FROM eclipse-temurin:17-jre
RUN mkdir /app
ADD ./target/backend-0.0.1-SNAPSHOT.jar ./app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]