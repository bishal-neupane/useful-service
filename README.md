# Random Facts Backend Service

This is a backend service built with Spring Boot and Java 17 that fetches random facts from the Useless Facts
API (`https://uselessfacts.jsph.pl/random.json?language=en`), provides a shortened URL for each fact, caches them, and offers a private area to
consult access statistics of the shortened url. The service is designed with a focus on clean and maintainable architecture.

## Table of Contents

- [Requirements](#requirements)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [Project Documentation and Reports](#project-documentation-and-reports)
- [API Endpoints](#api-endpoints)
- [Future Improvements](#future-improvements)
- [Time and Space Complexity](#time-and-space-complexity)
- [Contributors](#contributors)

## Requirements

- Java 17
- Maven 3.6+
- Internet connection to fetch dependencies

## Technologies Used

- Spring Boot
- Spring Security
- Maven for dependency management and reporting
- Resilience4j for retry logic
- In-memory caching
- RESTful API
- JUnit5, Mockito, AssertJ

## Setup Instructions

1. **Clone the repository:**
    ```bash
    git clone https://github.com/bishal-neupane/useful-service.git
    cd useful-service
    ```

2. **Install dependencies:**
    ```bash
    mvn clean install
    ```

3. **Configuration:**
    - No additional configuration is required. The application uses default configurations.

## Running the Application

To run the application, make sure java version is 17 and execute the following command in your terminal from the project root:

```bash
mvn spring-boot:run
```

Alternatively, the application can be run with a simple java command after running `mvn clean package`. To use java command type the following from
project root in your terminal:

```bash
java -jar target/useful-service-0.0.1-SNAPSHOT.jar
```

The application will start on the default port 8080. You can access it at http://localhost:8080.

## Project Documentation and Reports

Note: commands should be executed from the project root. All paths are also relative to the project root.\
Follow the below instructions for generating javadoc and test coverage reports locally and viewing them in a browser.

### Javadoc Generation

To generate Javadoc, execute the following Maven goal:

```bash
mvn javadoc:javadoc
```

The generated Javadoc will be located at the following default location:

```bash
target/site/apidocs/index.html
```

### JaCoCo Test Coverage Report

To generate and view JaCoCo test coverage report, execute following commands:

1. Run the tests to collect coverage data:

```bash
mvn clean test
```

2. Generate JaCoCo coverage report:

```bash
mvn jacoco:report
```

The generated JaCoCo coverage report will be at the following default location:

```bash
target/site/jacoco/index.html
```

## Endpoints

1. **Fetch Fact and Shorten URL**
    - **Endpoint**: `POST /facts`
    - **Description**: Fetches a random fact from the Useless Facts API, stores it, and returns a shortened URL.
    - **Response**:
      ```json
      {
        "original_fact": "string",
        "shortened_url": "string"
      }
      ```

2. **Redirect to Original Fact**
    - **Endpoint**: `GET /facts/{shortenedUrl}`
    - **Description**: Redirects to the original fact and increments the access count.

3. **Access Statistics**
    - **Endpoint**: `GET /admin/statistics`
    - **Description**: Provides access statistics for all shortened URLs.
    - **Requires**: Basic authentication using username and password (default: ~~admin/admin~~)
    - **Response**:
      ```json
      [
        {
          "shortened_url": "string",
          "access_count": "integer"
        }
      ]
      ```

## API Documentation

- Swagger UI: http://localhost:8080/swagger-ui/index.html
    - A visual interface to explore and interact with the API endpoints.

- OpenAPI v3 Specification: http://localhost:8080/v3/api-docs
    - The raw OpenAPI v3 specification in JSON format.

## Future Improvements

Here are some key areas I would like to focus on for further improvements.

1. #### Improve Authentication and Authorization
    - Implement more robust authentication and authorization mechanisms to enhance the security of private endpoints.
    - Avoid leaking security details from codebase.
2. #### Expand Test Coverage
    - Increase the number of tests to cover more edge cases and scenarios.
3. #### Containerization and Deployment
    - Set up a DNS entry for the application to make it accessible through a custom domain.
    - Deploy the application in a container orchestration environment such as Kubernetes to improve scalability, management and resilience.
4. #### Performance Optimization
    - Re-think caching strategy, perhaps make use of external store like Redis for improving statelessness.
    - Analyze and optimize the performance to handle higher loads efficiently.
5. #### Enhanced Logging and Monitoring
    - Improve logs, externalize access stats as metrics for better historical insights.
    - Expose application metrics to observe performance stats.
6. #### Refactor
    - Continuously refactor the codebase and improve readability, maintainability and enforce best practices.
7. #### Documentation
    - Improve and expand the project documentation to provide even clearer guidance.
    - Include more usage examples in docs.
    - Centralize javadoc and test coverage reports.

## Time and Space Complexity

Given *n* as the number of unique facts:\
**Time:**

- Average: On average, retrieval and put operations on the map is taking _O(1)_ time complexity. However, computation of the encoded short url takes\
  _O(log(n))_ considering base26 representation. So, overall it is _O(log(n))_.
- Worst: _O(n + log(n))_ due to collisions on the hashmap

**Space:** A Couple of maps store fact related data, so the overall space complexity is _O(n)_.

## Contributors

**Bishal Neupane**\
bishal.neup@gmail.com