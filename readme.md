### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8181/swagger-ui.html
- H2 UI : http://localhost:8181/h2-console
- Acutators : http://localhost:8181/actuator/health

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

- Technically advanced Java Developer with 6 years of experience in developing and delivering user-centric software applications using Java/J2EE technologies. Offering strong expertise in multiple programming skills, including, Spring MVC, Spring Boot, and XML, Angular 7(Fair Knowledge), Database, UML diagrams. Seeking to secure a challenging position as a Java Developer

# *Features Included in Employee Service*

### 1.  Rest Controller for Employee Service.
### 2.  Employee service class and its implementation.
### 3.  Spring security to protect rest endpoints.
### 5.  Swagger configuration for API documentation.
### 7.  Custom exception handlers and API responses and Util classes.
### 8.  Caching layer for enabling caching for this Service using Hazelcast configuration.
### 9.  Actuator framework support(out of the box features enabled) for Application health check.
### 10.  Logging using Sl4j framework.
### 11. JUnits support for implementation and business logic.


----------------------------------------------------------------------------
----------------------------------------------------------------------------

# Future enhancement which can be done

### 2. Integration testing.
### 3. UI integration.
### 4. Containerization(Docker).
### 5. SonarQube/SonarLint integration for code coverage/code optimization.
### 6. WebMVC configuration and Interceptors to enable CorrelationId injection in each request.(This is basically done to have and id )
### 7. Profile based configuration file.