FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn clean verify --fail-never -DskipTests
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY --from=build /app/target/lab1-0.0.1-SNAPSHOT.jar spring-boot-javalab.jar

EXPOSE 8080

CMD ["java", "-jar", "spring-boot-javalab.jar"]