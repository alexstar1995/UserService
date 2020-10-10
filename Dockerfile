FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/UserService-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]