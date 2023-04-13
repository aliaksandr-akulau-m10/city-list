# Cities API

This API allows:

- Browsing through the paginated list of cities with the corresponding photos
- Searching city by the name
- Editing city (both name and photo)
- Importing city data

## Technologies used

The Cities API service is a Maven based Java project.

### List of technologies and libraries:
* Java 17<br/>
* PostgreSQL<br/>
* Spring Boot 3<br/>
* Spring Data JDBC<br/>
* Spring Security
* Lombok<br/>
* JUnit 5<br/>
* TestContainers<br/>
* Mockito<br/>
* Mapstruct<br/>


### Usage
Http collection for tests is in **http** folder. To test API you can use maven spring boot plugin for instance.

```sh
mvn spring-boot:run
 ``` 

To spin up whole stack including Backend API service, Frontend Angular app and PostgreSQL run single command from the root of the project:

```sh
docker-compose up
 ``` 

To shut down
```sh
docker-compose down
```
