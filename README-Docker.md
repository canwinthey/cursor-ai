# Docker Setup for Student CRUD Application

This document explains how to build and run the Student CRUD application using Docker.

## Prerequisites

- Docker installed (version 20.10 or higher)
- Docker Compose installed (version 2.0 or higher) - optional but recommended
- Maven installed (for building the JAR file)
- Java 17 JDK installed

## Building the Application

**Important:** You must build the JAR file before building the Docker image.

### Step 1: Build the JAR file

```bash
mvn clean package
```

This will create the JAR file in the `target/` directory.

### Step 2: Build the Docker Image

#### Using Docker directly:

```bash
docker build -t student-crud:latest .
```

#### Using Docker Compose:

```bash
docker-compose build
```

**Note:** The Dockerfile expects the JAR file to be present in the `target/` directory. Make sure to run `mvn clean package` first.

## Running the Application

### Using Docker directly:

```bash
docker run -d -p 8080:8080 --name student-crud-app student-crud:latest
```

### Using Docker Compose (Recommended):

```bash
docker-compose up -d
```

To view logs:
```bash
docker-compose logs -f
```

## Accessing the Application

Once the container is running, you can access:

- **API Base URL**: http://localhost:8080/api/students
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:studentdb`
  - Username: `sa`
  - Password: (empty)
- **Health Check**: http://localhost:8080/actuator/health

## Docker Commands

### Stop the container:
```bash
docker-compose down
```

### Stop and remove volumes:
```bash
docker-compose down -v
```

### Rebuild and restart:
```bash
docker-compose up -d --build
```

### View container logs:
```bash
docker-compose logs -f student-app
```

### Execute commands in the container:
```bash
docker-compose exec student-app sh
```

## Docker Image Details

- **Base Image**: `eclipse-temurin:17-jre-alpine` (lightweight JRE)
- **Build Image**: `maven:3.9-eclipse-temurin-17` (for building)
- **Multi-stage Build**: Reduces final image size
- **Non-root User**: Application runs as non-root user for security
- **Health Check**: Built-in health check endpoint

## Environment Variables

You can customize the application using environment variables in `docker-compose.yml`:

- `SPRING_PROFILES_ACTIVE`: Spring profile (default: docker)
- `SPRING_DATASOURCE_URL`: Database URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

## Troubleshooting

### Container won't start:
```bash
docker-compose logs student-app
```

### Check container status:
```bash
docker-compose ps
```

### Restart the container:
```bash
docker-compose restart student-app
```

### Remove everything and start fresh:
```bash
docker-compose down -v
docker-compose up -d --build
```

## Production Considerations

For production deployment, consider:

1. **Use external database** (PostgreSQL, MySQL) instead of H2
2. **Add environment-specific configuration** using Spring profiles
3. **Configure proper logging** (file-based or centralized)
4. **Set up resource limits** in docker-compose.yml:
   ```yaml
   deploy:
     resources:
       limits:
         cpus: '1'
         memory: 512M
   ```
5. **Use secrets management** for sensitive data
6. **Enable HTTPS** with reverse proxy (nginx, traefik)
7. **Set up monitoring** and alerting

