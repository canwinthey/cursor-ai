# Student CRUD Application

A Spring Boot 3.4 application with REST API for managing Student entities.

## Technologies Used

- Java 17
- Spring Boot 3.4.0
- Spring Data JPA
- H2 Database (in-memory)
- Maven

## Project Structure

```
src/main/java/com/example/student/
├── StudentApplication.java          # Main application class
├── controller/
│   └── StudentController.java       # REST API endpoints
├── entity/
│   └── Student.java                 # Student entity
├── repository/
│   └── StudentRepository.java       # JPA repository
├── service/
│   └── StudentService.java          # Business logic
└── exception/
    ├── StudentNotFoundException.java
    └── GlobalExceptionHandler.java  # Exception handling
```

## Student Entity

The Student entity has the following fields:
- `id` (Long) - Primary key, auto-generated
- `name` (String) - Required, not blank
- `email` (String) - Required, valid email format, unique
- `age` (Integer) - Required, minimum value 1

## API Endpoints

### Get All Students
```
GET /api/students
```
Returns a list of all students.

### Get Student by ID
```
GET /api/students/{id}
```
Returns a student with the specified ID.

### Create Student
```
POST /api/students
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 20
}
```
Creates a new student and returns the created student with generated ID.

### Update Student
```
PUT /api/students/{id}
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "age": 22
}
```
Updates an existing student with the specified ID.

### Delete Student
```
DELETE /api/students/{id}
```
Deletes a student with the specified ID.

## Running the Application

1. Make sure you have Java 17 and Maven installed.

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

Or run the `StudentApplication` class directly from your IDE.

4. The application will start on `http://localhost:8080`

## H2 Console

The H2 database console is available at:
```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:studentdb`
- Username: `sa`
- Password: (empty)

## Example API Calls

### Create a Student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john.doe@example.com","age":20}'
```

### Get All Students
```bash
curl http://localhost:8080/api/students
```

### Get Student by ID
```bash
curl http://localhost:8080/api/students/1
```

### Update Student
```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane.doe@example.com","age":22}'
```

### Delete Student
```bash
curl -X DELETE http://localhost:8080/api/students/1
```

## Validation

The application includes validation for:
- Name: Required, cannot be blank
- Email: Required, must be a valid email format, must be unique
- Age: Required, must be at least 1

Invalid requests will return HTTP 400 (Bad Request) with validation error messages.

## Jenkins CI/CD Pipeline

The project includes a Jenkins pipeline (`Jenkinsfile`) for automated build, test, and deployment.

### Pipeline Stages

1. **Checkout** - Checks out the source code from the repository
2. **Build** - Compiles the application using Maven
3. **Test** - Runs unit tests and generates test reports
4. **Code Coverage Check** - Validates code coverage (minimum 80% required)
5. **Package** - Creates the JAR file
6. **SonarQube Analysis** - Performs code quality analysis (optional)
7. **Build Docker Image** - Builds the Docker image
8. **Docker Image Security Scan** - Scans for vulnerabilities (optional)
9. **Push Docker Image** - Pushes image to Docker registry (for main/master/develop branches)
10. **Deploy** - Deploys using Docker Compose (for main/master/develop branches)

### Jenkins Setup Requirements

1. **Install Required Plugins:**
   - Pipeline
   - Maven Integration
   - Docker Pipeline
   - JUnit
   - JaCoCo
   - SonarQube Scanner (optional)

2. **Configure Tools in Jenkins:**
   - Maven 3.9 (configure in `Manage Jenkins > Global Tool Configuration`)
   - JDK 17 (configure in `Manage Jenkins > Global Tool Configuration`)
   - Docker (ensure Docker is installed on Jenkins agent)

3. **Configure Credentials (Optional):**
   - `docker-registry-credentials`: Username/password for Docker registry
   - `docker-registry-url`: Docker registry URL (e.g., `docker.io`, `registry.example.com`)
   - `sonar-token`: SonarQube authentication token

4. **Create Pipeline Job:**
   - Create a new Pipeline job in Jenkins
   - Select "Pipeline script from SCM"
   - Choose your SCM (Git)
   - Set the repository URL
   - Set script path to `Jenkinsfile`

### Pipeline Features

- **Automatic Build**: Triggers on code commits
- **Test Execution**: Runs all unit tests and publishes results
- **Code Coverage**: Enforces 80% minimum code coverage
- **Docker Integration**: Builds and optionally pushes Docker images
- **Security Scanning**: Optional vulnerability scanning with Trivy or Docker Scout
- **Automatic Deployment**: Deploys to Docker Compose for main/master/develop branches
- **Artifact Archiving**: Archives JAR files and test reports

### Running the Pipeline

The pipeline will automatically run when:
- Code is pushed to the repository (if webhook is configured)
- Manually triggered from Jenkins UI
- Scheduled (if configured)

### Pipeline Environment Variables

You can customize the pipeline by modifying environment variables in the `Jenkinsfile`:
- `APP_NAME`: Application name (default: `student-crud`)
- `DOCKER_REGISTRY`: Docker registry URL
- `MAVEN_OPTS`: Maven JVM options

