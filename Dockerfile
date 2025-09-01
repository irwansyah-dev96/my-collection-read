# RUN
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/readcollection-read-0.0.1.jar  myapp.jar
COPY config/application-read.properties application.properties
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "myapp.jar"]

