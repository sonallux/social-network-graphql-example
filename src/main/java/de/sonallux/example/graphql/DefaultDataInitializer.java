package de.sonallux.example.graphql;

import de.sonallux.example.graphql.friendship.FriendshipService;
import de.sonallux.example.graphql.person.Person;
import de.sonallux.example.graphql.person.PersonService;
import de.sonallux.example.graphql.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DefaultDataInitializer {
    private final PersonService personService;
    private final FriendshipService friendshipService;
    private final PostService postService;

    @PostConstruct
    void init() {
        var jonas = new Person("4201", "Jonas", 25, "\uD83C\uDFD0 \uD83D\uDEB5 \uD83D\uDDA5️", "DATEV", "Software Engineer");
        var max = new Person("4202", "Max Mustermann");
        var erika = new Person("4203", "Erika Musterfrau");
        var hugo = new Person("4204", "Hugo First", 42, "King of the Firsts", "Kingdom of the Firsts", "King");

        personService.createPerson(jonas);
        personService.createPerson(max);
        personService.createPerson(erika);
        personService.createPerson(hugo);

        friendshipService.addFriendship(jonas.id(), max.id());
        friendshipService.addFriendship(jonas.id(), hugo.id());
        friendshipService.addFriendship(max.id(), erika.id());

        postService.createPost(jonas.id(), "Hallo Welt!", "Hiermit Grüße ich alle, die dabei sind!");
    }
}
