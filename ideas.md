# Ideas
Space for ideas about the implementation for UnityTest platform.

## Backend
Decided to go with a spring-boot microservice architecture for backend. This will serve to decouple the processing of test cases, courses and user endpoints.

### Research
Spring boot uses Maven as the build tool and is written in Java.

**Packages/Tools** needed:
* Web spring boot dependency for RESTful api endpoints
* H2 databases for local developement
* JPA package to automate queries and database connections
* Use Lombok library to reduce code for setters and getters
* Use Swagger to autogenerate API docs for front-end to consume

**TODOs**:
* Find a solution to generate source code docs -> to be pushed to Github pages as docs.

**For deployment**:
* Probably use an SQL database since easier to scale and support
* Need to decide on a platform to deploy application
* Figure out a CI/CD solutions for deployments

## Frontend
Either Angular or React web application