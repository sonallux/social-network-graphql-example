package de.sonallux.example.graphql.post;

import de.sonallux.example.graphql.person.Person;

public record PostLikedEvent(Post post, Person person) {
}
