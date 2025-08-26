# RUN
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/readcollection-read-0.0.1.jar  myapp.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "myapp.jar"]

