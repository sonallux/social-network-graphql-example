package de.sonallux.example.graphql.friendship;

import de.sonallux.example.graphql.util.EventEmitter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
class InMemoryFriendshipService implements FriendshipService {
    private final Map<String, List<String>> friendships = new LinkedHashMap<>();
    private final EventEmitter<FriendshipEvent> friendshipAddedEventEmitter = new EventEmitter<>();
    private final EventEmitter<FriendshipEvent> friendshipRemovedEventEmitter = new EventEmitter<>();

    @Override
    public List<String> getFriendIds(String personId) {
        return Collections.unmodifiableList(friendships.getOrDefault(personId, List.of()));
    }

    @Override
    public void addFriendship(String person1Id, String person2Id) {
        friendships.computeIfAbsent(person1Id, key -> new ArrayList<>()).add(person2Id);
        friendships.computeIfAbsent(person2Id, key -> new ArrayList<>()).add(person1Id);
        friendshipAddedEventEmitter.emitEvent(new FriendshipEvent(person1Id, person2Id));
    }

    @Override
    public void removeFriendship(String person1Id, String person2Id) {
        friendships.computeIfAbsent(person1Id, key -> new ArrayList<>()).remove(person2Id);
        friendships.computeIfAbsent(person2Id, key -> new ArrayList<>()).remove(person1Id);
        friendshipRemovedEventEmitter.emitEvent(new FriendshipEvent(person1Id, person2Id));
    }

    @Override
    public Flux<FriendshipEvent> friendshipAddedEvents() {
        return friendshipAddedEventEmitter.getEvents();
    }

    @Override
    public Flux<FriendshipEvent> friendshipRemovedEvents() {
        return friendshipRemovedEventEmitter.getEvents();
    }
}
