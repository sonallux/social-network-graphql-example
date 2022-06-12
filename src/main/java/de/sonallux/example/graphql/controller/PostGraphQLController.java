package de.sonallux.example.graphql.controller;

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

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostGraphQLController {
    private final PersonService personService;
    private final PostService postService;

    @QueryMapping
    public Optional<Post> post(@Argument String id) {
        return postService.getPost(id);
    }

    @SchemaMapping
    public Optional<Person> author(Post post) {
        return personService.getPerson(post.authorId());
    }

    @SchemaMapping
    public int likeCount(Post post) {
        return post.likes().size();
    }

    @SchemaMapping
    public List<Person> likes(Post post) {
        return post.likes().stream()
                .map(personService::getPerson)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<Post> createPost(@Argument String title, @Argument String content, Authentication authentication) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("title must not be empty");
        }
        if (content.isBlank()) {
            throw new IllegalArgumentException("content must not be empty");
        }

        var personId = authentication.getName();
        return Mono.just(postService.createPost(personId, title, content));
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<Post> likePost(@Argument String postId, Authentication authentication) {
        var personId = authentication.getName();
        return Mono.justOrEmpty(postService.likePost(postId, personId));
    }

    @SubscriptionMapping
    public Flux<Post> postCreated() {
        return postService.postCreatedEvents();
    }
}
