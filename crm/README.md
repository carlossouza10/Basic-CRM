# RestFul Standalone application that processes basic CRM operation.

- The application is based on Spring and most of its libraries by using Microservices Architecture concepts in order to create a lightweight enterprise service, highly scalable and available that runs independently from an external container but still provides enterprise ready features.

- The concern about code coverage is very relevant in that application. In order to guarantee 100% percent of code coverage, Jacoco has been configured during maven build. If the application doesn't have 100% of code coverage, the build will fail. After successful build, go on /target/site/index.html to see more details about project code coverage (Configuration classes were excluded from Jacoco's validation).

- In order to guarantee documentation of rest services, Swagger has been configured so that it gives us some documentation about the rest services and its methods, schemas, parameters, URL etc. After starting the application go on http://localhost:8080/swagger-ui.html. You can also get response from the application through this UI (using differents methods, parameters, url, etc.)

- This systems utilizes MySQL Server as Database to garantee integrity on the data storage. Open the file: src/main/resources/application.properties and set your own configurations for the database connection.


# Used libraries
- JDK-8
- Maven
- Spring Boot
- Swagger (rest documentation)

# Persistence
- MySQL for data persistence 
- Hibernate EntityManager

# Testing
- Junit
- Mockito - Library for creating mock objects/behaviour for unit tests-
- Mock-mvc-test - to test rest service from URL.
- Jacoco – Test code coverage.

# Build and run

- From terminal: (Java 8 and Maven 3 are required)
Go on the project's root folder, then type: mvn spring-boot:run

- From Eclipse:
Import as "Existing Maven Project" and run it as "Spring Boot App".

Follow bellow some of the services available in the application. To see more details, please get the application up and running and go on http://localhost:8080/swagger-ui.html.

#Services Endpoint

- GET http://localhost:8080/crm/api/v1/customers - Retrieves a list of customer
- GET http://localhost:8080/crm/api/v1/customers/1 - Retrieves a specific customer
- POST http://localhost:8080/crm/api/v1/customers - Creates a customer
- PUT http://localhost:8080/crm/api/v1/customers/1 - Updates a customer with ID 1
- PATCH http://localhost:8080/crm/api/v1/customers/1 - Updates a customer partially (no need to fill all fields on json)
- DELETE http://localhost:8080/crm/api/v1/customers/1 - Deletes a customer (and its appointments)
- GET http://localhost:8080/crm/api/v1/customers/1/appointments - Retrieves all appointments for customer with ID 1
- GET http://localhost:8080/crm/api/v1/customers/1/appointments/next - Retrieves the next appointment for customer with ID 1
- GET http://localhost:8080/crm/api/v1/customers/1/appointments/1 - Retrieves an appointment with ID 1 of the customer with ID 1
- POST http://localhost:8080/crm/api/v1/customers/1/appointments - Creates an appointment for customer with ID 1
- POST http://localhost:8080/crm/api/v1/customers/1/appointments/1/rating - Creates a rating to appointment with ID 1 of the customer with ID 1.
- GET http://localhost:8080/crm/api/v1/appointments - Retrieves all appointments for all customers
- GET http://localhost:8080/crm/api/v1/appointments/1 - Retrieves a specific appointment
- GET http://localhost:8080/crm/api/v1/appointments?weeks_ahead=1 - Retrieves all appointments of 1 week ahead (dynamic value).
