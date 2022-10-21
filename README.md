### requirements:

+ jdk 11 (or higher)
+ maven 3.8.6 (or higher)

Three profiles and append a -P parameter to switch which Maven profile will be applied:

* dev (default)
* test
* prod

How to run a local app, run following commands:

* mvn clean compile -U
* mvn spring-boot:run

How to (target folder):

* build - mvn clean package
* run local - java -jar <jarname>.jar

with profile

* build - mvn clean package -Pprod
* run local - java -jar <jarname>.jar

How to set db settings for profiles (local, dev, prod) in pom.xml

```xml

<properties>
    <spring.datasource.url>jdbc:postgresql://localhost:5432/postgres</spring.datasource.url>
    <spring.datasource.username>postgres</spring.datasource.username>
    <spring.datasource.password>postgres</spring.datasource.password>
</properties>
```

it needs for (application.properties)

```clojure
spring.datasource.url=@spring.datasource.url@
spring.datasource.username=@spring.datasource.username@
spring.datasource.password=@spring.datasource.password@
```

Api Documentation (Swagger)

* http://localhost:8080/api/swagger-ui/index.html (local host)
* http://host/context-path/api/swagger-ui/index.html

Install and run SonarQube

* docker pull sonarqube:latest
* docker container run -d -p 9000:9000 --name sonarserver sonarqube:latest

SonarQube verify

* mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
* mvn clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin

Spotbugs verify

* mvn com.github.spotbugs:spotbugs-maven-plugin:spotbugs
