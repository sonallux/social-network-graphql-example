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
        var markus = new Person("4205", "Markus Mustermann");
        var leon = new Person("4206", "Leon Mustermann");

        personService.createPerson(jonas);
        personService.createPerson(max);
        personService.createPerson(erika);
        personService.createPerson(hugo);
        personService.createPerson(markus);
        personService.createPerson(leon);

        friendshipService.addFriendship(jonas.id(), max.id());
        friendshipService.addFriendship(jonas.id(), markus.id());
        friendshipService.addFriendship(jonas.id(), hugo.id());
        friendshipService.addFriendship(max.id(), erika.id());
        friendshipService.addFriendship(max.id(), markus.id());
        friendshipService.addFriendship(max.id(), leon.id());
        friendshipService.addFriendship(erika.id(), markus.id());
        friendshipService.addFriendship(erika.id(), leon.id());

        var jonasPost = postService.createPost(jonas.id(), "Hallo Welt!", "Hiermit Grüße ich alle, die dabei sind!");
        postService.likePost(jonasPost.id(), hugo.id());

        var maxPost = postService.createPost(max.id(), "Wer bin ich?", "Ich bin eine fiktive Person, die oft auf Musterausweisen oder Dokumenten verwendet wird.");
        postService.likePost(maxPost.id(), erika.id());
        postService.likePost(maxPost.id(), leon.id());
    }
}
