package de.sonallux.example.graphql.post;

import de.sonallux.example.graphql.util.EventEmitter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.sonallux.example.graphql.util.IdGenerator.nextId;

@Service
class InMemoryPostService implements PostService {
    private final Map<String, Post> posts = new HashMap<>();
    private final EventEmitter<Post> postCreatedEventEmitter = new EventEmitter<>();
    private final EventEmitter<PostLikedEvent> postLikedEventEmitter = new EventEmitter<>();

    @Override
    public Optional<Post> getPost(String postId) {
        return Optional.ofNullable(posts.get(postId));
    }

    @Override
    public List<Post> getPostsOfPerson(String personId) {
        return posts.values().stream()
                .filter(post -> post.authorId().equals(personId))
                .toList();
    }

    @Override
    public Post createPost(String personId, String title, String content) {
        var post = new Post(nextId(), personId, title, content);
        posts.put(post.id(), post);
        postCreatedEventEmitter.emitEvent(post);
        return post;
    }

    @Override
    public Optional<Post> likePost(String postId, String personId) {
        return getPost(postId).map(post -> {
            post.likes().add(personId);
            return post;
        });
    }

    @Override
    public Flux<Post> postCreatedEvents() {
        return postCreatedEventEmitter.getEvents();
    }

    @Override
    public Flux<PostLikedEvent> postLikedEvents() {
        return postLikedEventEmitter.getEvents();
    }
}
