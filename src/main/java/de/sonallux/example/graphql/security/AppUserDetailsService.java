package de.sonallux.example.graphql.security;

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
        if (!personService.personExists(username)) {
            return Mono.empty();
        }
        return Mono.just(createUserDetails(username));
    }

    private UserDetails createUserDetails(String username) {
        return User.builder()
                .username(username)
                .password(username)
                .passwordEncoder(passwordEncoder::encode)
                .authorities("ROLE_USER")
                .build();
    }
}
