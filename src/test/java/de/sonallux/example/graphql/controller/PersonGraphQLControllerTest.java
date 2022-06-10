package de.sonallux.example.graphql.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

//@GraphQlTest
@SpringBootTest
@AutoConfigureGraphQlTester
public class PersonGraphQLControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void testGetPerson() {
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


}
