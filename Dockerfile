FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
COPY src src
RUN chmod +x mvnw && ./mvnw clean package -DskipTests -Dmaven.wagon.http.retryHandler.class=standard -Dmaven.wagon.http.retryHandler.count=5

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]