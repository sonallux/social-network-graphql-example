package de.sonallux.example.graphql.person;

public record Person(
        String id,
        String name,
        int age,
        String aboutMe,
        String employer,
        String position
) {
    public Person(String id, String name) {
        this(id, name, -1, null, null, null);
    }
}
