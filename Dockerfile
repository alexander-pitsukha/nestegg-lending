FROM maven:3.8.6-amazoncorretto-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests=true

FROM amazoncorretto:11-alpine3.15
COPY --from=build /usr/src/app/target/nestegg-lending-0.0.1-SNAPSHOT.jar /usr/app/nestegg-lending.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/nestegg-lending.jar"]