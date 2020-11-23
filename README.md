# UnityTest
UnityTest is an open souce platform where students can collectively write and run smoke tests on their assignments or project code for quick and easy sanity testing. Collaborators can either upload test cases or run their source code against the community-created test bank. Individual test cases can be upvoted or downvoted by collaborators to improve the overall quality of the pool. 

UnityTest will support projects written in Haskell, Python and Java with plans to support C, C++ and MySQL.

### Motivation
Let's face it. _Everyone writes their test cases last_.

Unless you're someone who lives by TDD, you're like the rest of us lazy developers and write your test cases as the last part of your assignment. But before you start writing you'll run several rounds of sanity checks to make sure your project works as expected.

Now imagine instead of only having the four quick cases you thought up, you also had the ones from your friends also working on the project. Or the ones from the entire class. These quick and dirty smoke tests can help you rat out bugs __before__ you start writing out the fancy test suite you're going to pretend you used to test your assignment.

We all want to make sure our assignment actually performs _according to the requirements_ before we submit it. After all, most of your marks come from the behavior of your code, _not_ the test case writeup.

## Setup

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

## Other

### Contributors
The UnityTest project is looking for contributors to join the initiative! Contact [Benjamin Kostiuk](mailto:benkostiuk1@gmail.com) if interested.

### Changelog
Regenerate the changelog with the following command:
```
git log --pretty="* %ad - %s @%h" --date=format:'%a %b %d %H:%M' > changelog.md
```