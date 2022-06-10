package de.sonallux.example.graphql.controller;

import de.sonallux.example.graphql.friendship.FriendshipEvent;
import de.sonallux.example.graphql.friendship.FriendshipService;
import de.sonallux.example.graphql.person.Person;
import de.sonallux.example.graphql.person.PersonService;
import de.sonallux.example.graphql.post.Post;
import de.sonallux.example.graphql.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PersonGraphQLController {
    private final PersonService personService;
    private final FriendshipService friendshipService;
    private final PostService postService;

    @QueryMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<Person> me(Authentication authentication) {
        var id = authentication.getName();
        return Mono.justOrEmpty(personService.getPerson(id));
    }

    @QueryMapping
    public Optional<Person> person(@Argument String id) {
        return personService.getPerson(id);
    }

    @QueryMapping
    public List<Person> searchPerson(@Argument String name) {
        return personService.searchPerson(name);
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<Integer> addFriendship(@Argument String personId, Authentication authentication) {
        personService.getPerson(personId).orElseThrow(() -> new IllegalArgumentException("Unknown person ID: " + personId));

        var myId = authentication.getName();
        if (myId.equals(personId)) {
            throw new IllegalArgumentException("Can not add friendship to one self");
        }

        friendshipService.addFriendship(myId, personId);
        return Mono.just(1);
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<Integer> removeFriendship(@Argument String personId, Authentication authentication) {
        personService.getPerson(personId).orElseThrow(() -> new IllegalArgumentException("Unknown person 1"));

        var myId = authentication.getName();
        friendshipService.removeFriendship(myId, personId);
        return Mono.just(1);
    }

    @SchemaMapping
    public List<Post> posts(Person person) {
        return postService.getPostsOfPerson(person.id());
    }

    @SchemaMapping
    public List<Person> friends(Person person) {
        return friendshipService.getFriendIds(person.id()).stream()
                .map(id -> personService.getPerson(id).orElse(null))
                .toList();
    }

    //@BatchMapping(typeName = "Person", field = "friends")
    public List<List<Person>> friends(List<Person> persons) {
        var friendships = new LinkedHashMap<Person, List<String>>();

        var requiredPersons = new HashSet<String>();

        for (Person person : persons) {
            var friends = friendshipService.getFriendIds(person.id());
            requiredPersons.addAll(friends);
            friendships.put(person, friends);
        }

        var allPersons = personService.getPersons(requiredPersons);

        return friendships.values().stream()
                .map(friends -> friends.stream().map(allPersons::get).toList())
                .toList();
    }

    @SubscriptionMapping
    public Flux<FriendshipEvent> friendshipAdded() {
        return friendshipService.friendshipAddedEvents();
    }

    @SubscriptionMapping
    public Flux<FriendshipEvent> friendshipRemoved() {
        return friendshipService.friendshipRemovedEvents();
    }
}
