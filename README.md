<h1 align="center">
  <img src="./logo.png" height="200"/><br>
  Puff
</h1>

<h4 align="center">Open source smoke testing platform</h4>

<!-- TODO Add link to platform >
<!-- <h4 align="center">Open source smoke testing platform | <a href="LINK" target="_blank">LINK</a></h4> -->

<p align="center">
  <a href="https://www.oracle.com/ca-en/java/technologies/javase/javase-jdk8-downloads.html" rel="nofollow"><img src="https://img.shields.io/badge/java-1.8-009ACD?style=flat-square&logo=Java" alt="java version" data-canonical-src="https://img.shields.io/badge/java-1.8-f39f37?style=flat-square&logo=Java" style="max-width:100%;"></a>
  <a href="https://spring.io/projects/spring-boot" rel="nofollow"><img src="https://img.shields.io/badge/spring--boot-3.2.0-6db33f?style=flat-square&logo=Spring" alt="spring boot version" data-canonical-src="https://img.shields.io/badge/spring--boot-3.2.0-6db33f?style=flat-square&logo=Spring" style="max-width:100%;"></a>
  <a href="https://swagger.io" rel="nofollow"><img src="https://img.shields.io/badge/swagger-2.0-6c9a00?style=flat-square&logo=Swagger" alt="swagger version" data-canonical-src="https://img.shields.io/badge/swagger-2.0-6c9a00?style=flat-square&logo=Swagger" style="max-width:100%;"></a>
</p>

<blockquote align="center">
  <em>Puff</em> is an open source smoke testing platform for students to collaboratively write and run tests on their assignment or project code for quick and easy sanity testing.
</blockquote>

<p align="center">
  <a href="#getting-started">Getting started</a>&nbsp;|&nbsp;<a href="#motivation">Motivation</a>&nbsp;|&nbsp;<a href="#supported-languages">Supported Languages</a>&nbsp;|&nbsp;<a href="#contributors">Contributors</a>
</p>

## Motivation
Let's face it. _Everyone writes their test cases last_.

Unless you're someone who lives by [TDD](https://en.wikipedia.org/wiki/Test-driven_development), you're like the rest of us lazy developers and write your test cases as the last part of your assignment. But before you start writing you'll run several rounds of sanity checks (_smoke tests_) to make sure your project works as expected.

Now imagine instead of only having the four quick cases you thought up, you also had the ones from your friends also working on the project. Or the ones from the entire class. These quick and dirty smoke tests can help you rat out bugs __before__ you start writing out the fancy test suite you're going to pretend you used to test your assignment.

We all want to make sure our assignment actually performs _according to the requirements_ before we submit it. After all, most of your marks come from the behavior of your code, _not_ the test case writeup.


## Getting started
Clone the project with `git clone https://github.com/benjaminkostiuk/unity-test.git`

### Install Java
_Puff_ uses a microservice architecture with [Spring Boot](https://spring.io/projects/spring-boot) written in Java.

* Download and install the [Java JDK 8](https://www.oracle.com/ca-en/java/technologies/javase/javase-jdk8-downloads.html)
* Set the `JAVA_HOME` environment variable
* Verify your installation by running `java -version`

### Install Maven
_Puff_ uses [Maven](https://maven.apache.org/) as its build tool for its microservice backend.
* [Download](https://maven.apache.org/download.cgi) and [install](https://maven.apache.org/install.html) Maven
* Verify your installation with `mvn -v`

### Setup Keycloak
> Keycloak is an open source Identity and Access Management solution aimed at modern applications and services. It makes it easy to secure applications and services with little to no code.

_Puff_ uses keycloak as a user management and authentication solution. More information about Keycloak can be found on their [offical docs page](https://www.keycloak.org/docs/latest/index.html).

* Download and install the standalone server from https://www.keycloak.org/downloads.html.
* Navigate to your installed keycloak directory and run
```shell
./bin/standalone.sh -Djboss.socket.binding.port-offset=100
```
* Setup your admin account by navigating to http://localhost:8180.
* Navigate to the admin portal from http://localhost:8180/auth/admin
* Import the realm settings, configuration and clients using the [puff-keycloak-config.json](security/puff-keycloak-config.json) file located in the security folder.

#### Setup test account
* Create two test user accounts and add one each to the `Users` and `Administrators` groups.
* You can optionally add the `sys` role to a user to test system-secured endpoints.
* View the test account page at http://localhost:8180/auth/realms/puff/account/.
* Generate an authentication token by making the following curl call **replacing TEST_USER_USERNAME** and **TEST_USER_PASSWORD** with the credentials for the test accounts you created.
```shell
curl -X POST 'http://localhost:8180/auth/realms/puff/protocol/openid-connect/token' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode 'grant_type=password' \
 --data-urlencode 'client_id=user-auth' \
 --data-urlencode 'client_secret=USER_AUTH_CLIENT_SECRET' \
 --data-urlencode 'username=TEST_USER_USERNAME' \
 --data-urlencode 'password=TEST_USER_PASSWORD'
```
### Run the backend
_Puff_ has 3 microservices that make up its backend:
* **course-management** manages courses, assignments and user actions
* **test-runner** runs test cases and manages user code uploads
* **user-management** manages user permissions and access controls

In order to run a microservice locally

* Navigate to the microservice with folder `cd backend/microservice_name`.
* Run the microservice with 
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
* If you need to build the `.jar` of the application run `mvn package`.

## Development

### Swagger
_Puff_'s Spring-Boot backend exposes a REST API. The project utilizes [Swagger](https://swagger.io/) to document and keep a consistent REST interface.

Once you have a microservice running (See [run the backend](#run-the-backend)) visit the following urls to see the REST APIs for the respective microservices:
* Course-management: http://localhost:8080/swagger-ui.html
* Test-runner: http://locahost:8083/swagger-ui.html
* User-management: TBD

A `json` api version to be consumed and used to generate client libraries can be accessed at http://localhost:XXXX/v2/api-docs.

Select `Authorize` and login with a test user account to try out any of the endpoints.

### H2 Database
_Puff_'s Spring-boot backend uses a H2 runtime database to simulate a database connection for local development. Once the project is running it can be accessed at 
* Course-management: http://localhost:8080/h2
* Test-runner: http://localhost:8083/h2

The credentials for the database are as follows:
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: admin
Password:
```

For more information about H2 databases see the [H2 Database Engine](https://www.h2database.com/html/main.html).
## Docs
Included in the `docs/` folder are documents with the planning for API endpoints specifications and entity relation diagrams for UnityTest's data model. 

More information will be added as the project matures.

## Supported languages
The _Puff_ testing platform plans to support projects written in the following languages:

### Haskell
Quickcheck and simple GHCi cases.

### Python
Tests written using pytest.

### Java
Tests written using JUnit.

### To come
C, C++, MySQL

## Contributors
The _Puff_ project is looking for contributors to join the initiative!
For information about progress, features under construction and opportunities to contribute see [our project board](https://github.com/benjaminkostiuk/unity-test/projects/1).


If you're interested in helping please read our [CONTRIBUTING.md](./CONTRIBUTING.md) for details like our Code of Conduct and contact [Benjamin Kostiuk](mailto:benkostiuk1@gmail.com) for more information.