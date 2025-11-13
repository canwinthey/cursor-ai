# Docker and CI/CD Setup

## Docker Setup

### Prerequisites
- Docker installed and running
- Maven installed (for building the application)

### Building the Docker Image

The build process is kept outside the Dockerfile as requested. Follow these steps:

1. **Build the Maven project:**
   ```bash
   mvn clean package
   ```

2. **Build the Docker image:**
   ```bash
   docker build -t cursor-crud-product:latest .
   ```

   Or use the provided build script:
   ```bash
   chmod +x build-docker.sh
   ./build-docker.sh
   ```

### Running the Docker Container

```bash
docker run -d -p 9090:9090 --name cursor-crud-product-container cursor-crud-product:latest
```

### Accessing the Application

- API Base URL: `http://localhost:9090/api/product`
- Swagger UI: `http://localhost:9090/swagger-ui.html`
- H2 Console: `http://localhost:9090/h2-console`
- Actuator: `http://localhost:9090/actuator`

### Stopping and Removing Container

```bash
docker stop cursor-crud-product-container
docker rm cursor-crud-product-container
```

## Jenkins Pipeline

### Prerequisites
- Jenkins server installed and running
- Docker installed on Jenkins server
- Maven and JDK 17 configured in Jenkins
- HTML Publisher Plugin installed in Jenkins
- JaCoCo Plugin installed in Jenkins

### Pipeline Configuration

1. **Create a new Pipeline job in Jenkins:**
   - Go to Jenkins Dashboard
   - Click "New Item"
   - Enter job name (e.g., "cursor-crud-product-pipeline")
   - Select "Pipeline" and click OK

2. **Configure the Pipeline:**
   - In Pipeline definition, select "Pipeline script from SCM"
   - SCM: Git
   - Repository URL: Your Git repository URL
   - Branch: */main (or your branch)
   - Script Path: Jenkinsfile

3. **Configure Tools:**
   - Ensure Maven and JDK17 are configured in Jenkins Global Tool Configuration
   - Go to Manage Jenkins → Global Tool Configuration
   - Configure Maven and JDK 17

### Pipeline Stages

The Jenkins pipeline includes the following stages:

1. **Checkout**: Checks out code from repository
2. **Build**: Compiles and packages the application (`mvn clean package -DskipTests`)
3. **Test**: Runs JUnit 5 tests (`mvn test`)
4. **Generate Coverage Report**: Generates JaCoCo coverage report (`mvn jacoco:report`)
5. **Build Docker Image**: Builds Docker image locally (does NOT push to DockerHub)
6. **Deploy**: Deploys application using local Docker image

### Accessing Coverage Reports

After the pipeline runs successfully, you can access the coverage reports at:

**JaCoCo Coverage Report URL:**
```
http://<jenkins-server-url>/job/<job-name>/<build-number>/HTML_Report/
```

**Example:**
```
http://localhost:8080/job/cursor-crud-product-pipeline/1/HTML_Report/
```

Or from the Jenkins build page:
- Navigate to the build
- Click on "JaCoCo Coverage Report" in the left sidebar

### Test Results

JUnit test results are published automatically and can be viewed at:
```
http://<jenkins-server-url>/job/<job-name>/<build-number>/testReport/
```

### Notes

- The Docker image is built locally and NOT pushed to DockerHub
- The application is deployed using the locally built Docker image
- Coverage reports are stored on the Jenkins server and accessible via the HTML Report link
- The pipeline stops and removes any existing container before deploying a new one

