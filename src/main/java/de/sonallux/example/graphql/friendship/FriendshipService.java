package de.sonallux.example.graphql.friendship;

import reactor.core.publisher.Flux;

import java.util.List;

public interface FriendshipService {
    List<String> getFriendIds(String personId);
    void addFriendship(String person1Id, String person2Id);
    void removeFriendship(String person1Id, String person2Id);

    Flux<FriendshipEvent> friendshipAddedEvents();
    Flux<FriendshipEvent> friendshipRemovedEvents();
}
