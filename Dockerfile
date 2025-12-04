# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven descriptor
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy project files
COPY . .

# Build the project (skip tests for speed)
RUN mvn -q clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
