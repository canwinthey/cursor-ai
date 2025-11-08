# Runtime image for Spring Boot application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install wget for health checks and create non-root user
RUN apk add --no-cache wget && \
    addgroup -S spring && \
    adduser -S spring -G spring

# Switch to non-root user for security
USER spring:spring

# Copy the pre-built JAR file from local target directory
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 9090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

