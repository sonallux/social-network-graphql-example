# Social network - Spring GraphQL example

## Documentation
- [spring-graphql](https://docs.spring.io/spring-graphql/docs/1.0.0/reference/html/)
- [spring-boot-graphql-starter](https://docs.spring.io/spring-boot/docs/2.7.0/reference/html/web.html#web.graphql)

## Setup

After starting the Spring Boot Application a [GraphiQL](https://github.com/graphql/graphiql/tree/main/packages/graphiql#readme) instance is available at http://localhost:8080/graphiql: The GraphQL schema can be obtained from http://localhost:8080/graphql/schema.

## Authentication

Some queries require basic authentication. The password is equal to the username.

To use authentication in GraphiQL add the following JSON under "request header":

- Jonas: `{"Authorization": "Basic NDIwMTo0MjAx"}`
- Max Mustermann: `{"Authorization": "Basic NDIwMjo0MjAy"}`
- Erika Musterfrau: `{"Authorization": "Basic NDIwMzo0MjAz"}`
- Hugo: `{"Authorization": "Basic NDIwNDo0MjA0"}`
