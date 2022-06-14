package de.sonallux.example.graphql.controller;

import de.sonallux.example.graphql.friendship.FriendshipEvent;
import de.sonallux.example.graphql.friendship.FriendshipService;
import de.sonallux.example.graphql.person.Person;
import de.sonallux.example.graphql.person.PersonService;
import de.sonallux.example.graphql.post.Post;
import de.sonallux.example.graphql.post.PostService;
import de.sonallux.example.graphql.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
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

    @SchemaMapping
    public List<Post> posts(Person person) {
        return postService.getPostsOfPerson(person.id());
    }

    @SchemaMapping
    public List<Person> friends(Person person) {
        return friendshipService.getFriendIds(person.id()).stream()
                .map(personService::getPerson)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    //@BatchMapping(typeName = "Person", field = "friends")
    public List<List<Person>> friends(List<Person> persons) {
        var requiredPersons = new HashSet<String>();

        var friendships = persons.stream()
                .map(person -> {
                    var friends = friendshipService.getFriendIds(person.id());
                    requiredPersons.addAll(friends);
                    return friends;
                })
                .toList();

        var allPersons = personService.getPersons(requiredPersons);

        return friendships.stream()
                .map(friends -> friends.stream().map(allPersons::get).toList())
                .toList();
    }

    @MutationMapping
    public Person createPerson(@Argument String name, @Argument Integer age, @Argument String aboutMe,
                               @Argument String employer, @Argument String position) {
        var person = new Person(IdGenerator.nextId(), name, age, aboutMe, employer, position);
        personService.createPerson(person);
        return person;
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<Integer> addFriendship(@Argument String personId, Authentication authentication) {
        if (!personService.personExists(personId)) {
            throw new IllegalArgumentException("Unknown person id: " + personId);
        }

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
        if (!personService.personExists(personId)) {
            throw new IllegalArgumentException("Unknown person id: " + personId);
        }

        var myId = authentication.getName();
        friendshipService.removeFriendship(myId, personId);
        return Mono.just(1);
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
