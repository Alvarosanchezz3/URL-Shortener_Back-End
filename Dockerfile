FROM openjdk:21-oracle
COPY target/URLShortener-0.0.1-SNAPSHOT.jar java-app.jar
ENTRYPOINT  ["java", "-jar", "java-app.jar"]