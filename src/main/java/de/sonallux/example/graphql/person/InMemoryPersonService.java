package de.sonallux.example.graphql.person;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class InMemoryPersonService implements PersonService {
    private final HashMap<String, Person> persons = new HashMap<>();

    @Override
    public Optional<Person> getPerson(String id) {
        return Optional.ofNullable(persons.get(id));
    }

    @Override
    public Map<String, Person> getPersons(Collection<String> ids) {
        return ids.stream()
                .map(this::getPerson)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Person::id, Function.identity()));
    }

    @Override
    public List<Person> searchPerson(String name) {
        return persons.values().stream()
                .filter(person -> person.name().contains(name))
                .toList();
    }

    @Override
    public void createPerson(Person person) {
        persons.put(person.id(), person);
    }
}
