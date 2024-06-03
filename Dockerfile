# Use a base image with Alpine Linux and OpenJDK 17 for building
FROM openjdk:17-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files and install Maven
COPY pom.xml ./
RUN apk add --no-cache maven

# Download dependencies and cache them in Docker layer
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Package the application (skip tests)
RUN mvn package -DskipTests

# Create a lightweight final image with the JAR file
FROM openjdk:17-alpine

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 9095

# Command to run the application
CMD ["java", "-jar", "app.jar"]