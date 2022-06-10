package de.sonallux.example.graphql.post;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> getPost(String postId);
    List<Post> getPostsOfPerson(String personId);
    Post createPost(String personId, String title, String content);
    Optional<Post> likePost(String postId, String personId);

    Flux<Post> postCreatedEvents();
    Flux<PostLikedEvent> postLikedEvents();
}
