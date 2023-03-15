FROM eclipse-temurin:17-jdk-jammy as base
WORKDIR /realworld
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as devolopment
CMD ["./mvnw", "spring-boot:run"]

FROM base as build
RUN ./mvnw package -Dmaven.test.skip

FROM eclipse-temurin:17-jre-jammy as production
EXPOSE 8080
COPY --from=build /realworld/target/conduit-api-docker.jar ./conduit-api-docker.jar
CMD ["java", "-jar", "/conduit-api-docker.jar"]