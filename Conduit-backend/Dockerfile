FROM eclipse-temurin:17-jdk-jammy as base
FROM maven:3.9.0-eclipse-temurin-17-alpine as maven
WORKDIR /realworld
COPY .mvn/ .mvn
COPY pom.xml ./
RUN mvn dependency:resolve
COPY src ./src

FROM base as devolopment
CMD ["mvn", "spring-boot:run"]

FROM maven as build
RUN mvn package -Dmaven.test.skip

FROM eclipse-temurin:17-jre-jammy as production
EXPOSE 8080
COPY --from=build /realworld/target/conduit-api-docker.jar ./conduit-api-docker.jar
CMD ["java", "-jar", "/conduit-api-docker.jar"]