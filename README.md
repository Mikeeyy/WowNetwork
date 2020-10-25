I. **Implemented application scenarios**

Posting

A user should be able to post a 140 character message.

Wall

A user should be able to see a list of the messages they've posted, in reverse chronological order.

Following

A user should be able to follow another user. Following doesn't have to be reciprocal: Alice can follow Bob without Bob having to follow Alice.

Timeline

A user should be able to see a list of the messages posted by all the people they follow, in reverse chronological order.

II. **How to run it**

Using docker-compose:
- execute: gradlew build
- execute: docker-compose up


III. **REST Api documentation - Swagger.**

Api is documented using Swagger - all of the endpoints, models, properties, fields are described using annotations.
To visit documentation and have possibility to invoke apis one can visit: http://localhost:8589/swagger-ui.html

VI. **Technology stack:**

- spring boot & tomcat
- H2 database
- hibernate
- spring-data
- spring-test
- swagger (springfox)
- lombok
- junit5
- assertj
- mockito
