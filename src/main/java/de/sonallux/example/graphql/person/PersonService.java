package de.sonallux.example.graphql.person;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PersonService {
    boolean personExists(String id);
    Optional<Person> getPerson(String id);
    Map<String, Person> getPersons(Collection<String> ids);
    List<Person> searchPerson(String name);
    void createPerson(Person person);
}
