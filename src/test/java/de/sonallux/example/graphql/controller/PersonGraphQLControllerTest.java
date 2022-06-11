package de.sonallux.example.graphql.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

//@GraphQlTest
@SpringBootTest
@AutoConfigureGraphQlTester
public class PersonGraphQLControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void testQueryPerson() {
        graphQlTester.document("""
            query{person(id: "4201"){id name age aboutMe employer position}}
            """)
                .execute()
                .errors().satisfy(errors -> assertThat(errors).isEmpty())
                .path("person.id").entity(String.class).isEqualTo("4201")
                .path("person.name").entity(String.class).isEqualTo("Jonas")
                .path("person.age").entity(Integer.class).isEqualTo(25)
                .path("person.aboutMe").entity(String.class).isEqualTo("\uD83C\uDFD0 \uD83D\uDEB5 \uD83D\uDDA5️")
                .path("person.employer").entity(String.class).isEqualTo("DATEV")
                .path("person.position").entity(String.class).isEqualTo("Software Engineer");
    }

    @Test
    @WithMockUser(username = "4201")
    void testQueryMe() {
        graphQlTester.document("""
            query{me{id name}}
            """)
                .execute()
                .errors().satisfy(errors -> assertThat(errors).isEmpty())
                .path("me.id").entity(String.class).isEqualTo("4201")
                .path("me.name").entity(String.class).isEqualTo("Jonas");
    }

    @Test
    void testQueryMeFailsOnMissingAuthentication() {
        graphQlTester.document("""
            query{me{id name}}
            """)
                .execute()
                .errors()
                .expect(error -> error.getErrorType() == ErrorType.UNAUTHORIZED)
                .verify();
    }

    @Test
    void testQueryFriendsOfPerson() {
        graphQlTester.document("""
            query{person(id: "4201"){id friends{id name}}}
            """)
                .execute()
                .errors().satisfy(errors -> assertThat(errors).isEmpty())
                .path("person.id").entity(String.class).isEqualTo("4201")
                .path("person.friends[0].id").entity(String.class).isEqualTo("4202")
                .path("person.friends[0].name").entity(String.class).isEqualTo("Max Mustermann")
                .path("person.friends[1].id").entity(String.class).isEqualTo("4204")
                .path("person.friends[1].name").entity(String.class).isEqualTo("Hugo First");
    }
}
