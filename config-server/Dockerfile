# Step of building the application
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Copy of maven configuration file
COPY pom.xml .
# Copy of source code
COPY src ./src
# Build of the application
RUN mvn clean package

# Build of the Docker image
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=build /app/target/*.jar app.jar
USER appuser
ENTRYPOINT ["java","-jar","/app.jar"]