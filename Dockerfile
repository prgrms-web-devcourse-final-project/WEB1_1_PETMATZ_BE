# 1. Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the built JAR file into the container
# Replace 'build/libs' with your actual build output directory
COPY build/libs/*.jar app.jar

# 4. Expose the application port
EXPOSE 8080

# 5. Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
