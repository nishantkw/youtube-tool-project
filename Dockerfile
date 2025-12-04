# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven descriptor from subfolder
COPY youtubetools/pom.xml pom.xml
RUN mvn -q dependency:go-offline

# Copy project files
COPY youtubetools/ ./youtubetools/

# Build the project (skip tests for speed)
RUN mvn -f youtubetools/pom.xml -q clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/youtubetools/target/*.jar app.jar

EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
