# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jre-jammy

ARG JAR_FILE=target/*.jar

# Set working directory
#WORKDIR /app

# Copy the built JAR file from target directory
# Note: The JAR file should be built before Docker build using: mvn clean package
COPY ${JAR_FILE} app.jar

# Expose port 9090
EXPOSE 9090

# Set JVM options for better container performance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

