package de.sonallux.example.graphql.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
class InMemoryPersonService implements PersonService {
    private final HashMap<String, Person> persons = new HashMap<>();

    @Override
    public boolean personExists(String id) {
        return persons.containsKey(id);
    }

    @Override
    public Optional<Person> getPerson(String id) {
        log.info("getPerson: {}", id);
        return Optional.ofNullable(persons.get(id));
    }

    @Override
    public Map<String, Person> getPersons(Collection<String> ids) {
        log.info("getPersons: {}", ids);
        return ids.stream()
                .map(persons::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Person::id, Function.identity()));
    }

    @Override
    public List<Person> searchPerson(String name) {
        log.info("searchPerson: {}", name);
        return persons.values().stream()
                .filter(person -> person.name().contains(name))
                .toList();
    }

    @Override
    public void createPerson(Person person) {
        persons.put(person.id(), person);
    }
}
