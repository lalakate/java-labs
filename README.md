# List of universities by country name
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lalakate_java-labs&metric=alert_status)](https://sonarcloud.io/summary/overall?id=lalakate_java-labs)

This API provides endpoints to show list of universities filtered by country. 
Endpoints have been implemented for the creation and management of universities, countries and students and save data to database.

## Used stack

- OpenJDK 21
- Spring Boot 3.2.3
- Maven
- PostgreSQL 16

## Request example

```bash
localhost:8080/api/v1/university?country={country}
```