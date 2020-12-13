<h1 align="center">
  <img src="./puff_logo.png" height="300"/>
  <br>
  Puff
</h1>

<h4 align="center">Open source smoke testing platform</h4>

<!-- TODO Add link to platform >
<!-- <h4 align="center">Open source smoke testing platform | <a href="LINK" target="_blank">LINK</a></h4> -->

<p align="center">
  <a href="https://www.oracle.com/ca-en/java/technologies/javase/javase-jdk8-downloads.html" rel="nofollow"><img src="https://img.shields.io/badge/java-1.8-009ACD?style=flat-square&logo=Java" alt="java version" data-canonical-src="https://img.shields.io/badge/java-1.8-f39f37?style=flat-square&logo=Java" style="max-width:100%;"></a>
  <a href="" rel="nofollow"><img src="https://img.shields.io/badge/spring--boot-v3.2.0-6db33f?style=flat-square&logo=Spring" alt="java version" data-canonical-src="https://img.shields.io/badge/java-1.8-f39f37?style=flat-square&logo=Java" style="max-width:100%;"></a>
  <a href="" rel="nofollow"><img src="https://img.shields.io/badge/swagger-2.0-6c9a00?style=flat-square&logo=Swagger" alt="java version" data-canonical-src="https://img.shields.io/badge/java-1.8-f39f37?style=flat-square&logo=Java" style="max-width:100%;"></a>
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

### Install maven
This project uses Spring-boot applications as a backend as `maven` as its build too.

* Follow [these steps](https://maven.apache.org/install.html) to install maven
* Check your version with `mvn -v`
* Ensure your `JAVA_HOME` env variable is set.

### Clone the project
* Clone the project with `git clone https://github.com/benjaminkostiuk/unity-test.git`

### Run the server
* Navigate to the `server` folder with `cd server`.
* Run the server with `mvn spring-boot:run`.
* If you need to build the `.jar` of the application run `mvn package`.

## Development

### Swagger
UnityTest's Spring-boot backend exposes a REST interface to be consumed by its front-end. The project utilizes [Swagger](https://swagger.io/) to document and keep a consistent REST interface.

Run the backend with `mvn spring-boot:run` and visit http://localhost:8080/swagger-ui.html to see the REST api docs. A `json` version to be consumed and used to generate client libraries can be accessed at http://localhost:8080/v2/api-docs. 

### H2 Database
UnityTest's Spring-boot backend uses a H2 runtime database to simulate a database connection for local development. Once the project is running it can be accessed at http://localhost:8080/h2.

The credentials are as follows:
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: admin
Password:
```

For more information about H2 databases see the [H2 Database Engine](https://www.h2database.com/html/main.html).
## Docs
Included in the `docs/` folder are documents with the planning for API endpoints specifications and entity relation diagrams for UnityTest's data model.


## Supported languages
UnityTest will support projects written in Haskell, Python and Java with plans to support C, C++ and MySQL.

## Contributors
The UnityTest project is looking for contributors to join the initiative! Contact [Benjamin Kostiuk](mailto:benkostiuk1@gmail.com) if interested.