package de.sonallux.example.graphql.security;

import de.sonallux.example.graphql.person.Person;
import de.sonallux.example.graphql.person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppUserDetailsService implements ReactiveUserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final PersonService personService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.justOrEmpty(personService.getPerson(username))
                .map(this::createUserDetails);
    }

    private UserDetails createUserDetails(Person person) {
        return User.builder()
                .username(person.id())
                .password(person.id())
                .passwordEncoder(passwordEncoder::encode)
                .authorities("ROLE_USER")
                .build();
    }
}
