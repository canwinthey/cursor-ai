# Jenkins Local Deployment Setup Guide

This guide will help you set up Jenkins for local deployment of the Student CRUD application.

## Prerequisites

Before starting, ensure you have:
- Java 17 JDK installed
- Maven 3.9+ installed
- Docker and Docker Compose installed
- Jenkins installed (or ready to install)

## Step 1: Install Jenkins

### Option A: Install Jenkins on Windows

1. Download Jenkins from https://www.jenkins.io/download/
2. Download the Windows installer (jenkins.msi)
3. Run the installer and follow the setup wizard
4. Jenkins will start automatically on `http://localhost:8080`

### Option B: Run Jenkins using Docker

```bash
docker run -d -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  --name jenkins \
  jenkins/jenkins:lts
```

Access Jenkins at `http://localhost:8080`

### Initial Jenkins Setup

1. Open `http://localhost:8080` in your browser
2. Get the initial admin password:
   - Windows: Check `C:\Program Files\Jenkins\secrets\initialAdminPassword`
   - Docker: Run `docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword`
3. Install suggested plugins
4. Create an admin user

## Step 2: Install Required Jenkins Plugins

1. Go to **Manage Jenkins** > **Manage Plugins**
2. Click on **Available** tab
3. Search and install the following plugins:
   - **Pipeline** (usually pre-installed)
   - **Maven Integration**
   - **Docker Pipeline**
   - **JUnit**
   - **JaCoCo**
   - **HTML Publisher** (for test reports)
   - **Workspace Cleanup** (optional)

4. Click **Install without restart** or **Download now and install after restart**
5. Restart Jenkins if prompted

## Step 3: Configure Tools in Jenkins

1. Go to **Manage Jenkins** > **Global Tool Configuration**

2. **Configure Maven:**
   - Click **Add Maven**
   - Name: `Maven-3.9`
   - Check **Install automatically**
   - Version: Select `3.9.6` or latest
   - Click **Save**

3. **Configure JDK:**
   - Under **JDK**, click **Add JDK**
   - Name: `JDK-17`
   - Check **Install automatically**
   - Version: Select `17` (e.g., `17.0.2+8`)
   - Click **Save**

   **OR** if you have JDK installed locally:
   - Name: `JDK-17`
   - Uncheck **Install automatically**
   - JAVA_HOME: `C:\Program Files\Java\jdk-17` (adjust to your path)

## Step 4: Configure Docker Access

### For Windows Jenkins

1. Ensure Docker Desktop is running
2. Jenkins needs access to Docker. You may need to:
   - Add Jenkins service user to Docker group, OR
   - Configure Jenkins to run Docker commands with proper permissions

3. Test Docker access:
   - Go to **Manage Jenkins** > **Script Console**
   - Run: `"docker --version".execute().text`
   - Should return Docker version

### For Docker Jenkins

If Jenkins is running in Docker, you need to mount Docker socket:

```bash
# Stop existing Jenkins container
docker stop jenkins
docker rm jenkins

# Run Jenkins with Docker socket access
docker run -d -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --name jenkins \
  jenkins/jenkins:lts
```

## Step 5: Create Jenkins Pipeline Job

### Method 1: Using Pipeline Script (Recommended for Local)

1. Go to Jenkins dashboard
2. Click **New Item**
3. Enter job name: `student-crud-pipeline`
4. Select **Pipeline**
5. Click **OK**

6. In the job configuration:
   - **Description**: `Student CRUD Application CI/CD Pipeline`
   
   - **Pipeline Definition**: Select **Pipeline script**
   
   - **Script**: Copy the entire contents of `Jenkinsfile` from your project
   
   - **Advanced Options** (optional):
     - **Script Path**: Leave as `Jenkinsfile` (if using SCM later)
   
   - Click **Save**

### Method 2: Using Local Workspace

1. Create the pipeline job as above
2. Instead of copying script, configure:
   - **Pipeline Definition**: Select **Pipeline script from SCM**
   - **SCM**: Select **None** or **File System**
   - **Script Path**: Point to your local `Jenkinsfile`

### Method 3: Copy Source Code to Jenkins Workspace

1. Find Jenkins workspace directory:
   - Windows: Usually `C:\Program Files\Jenkins\workspace\`
   - Docker: `/var/jenkins_home/workspace/`

2. Create a folder: `student-crud-pipeline`

3. Copy your entire project to this folder:
   ```bash
   # Example (adjust paths as needed)
   xcopy /E /I "C:\cursor-ai\basic-sb-proj" "C:\Program Files\Jenkins\workspace\student-crud-pipeline"
   ```

4. Create pipeline job and use **Pipeline script from SCM** with **File System**

## Step 6: Configure Pipeline for Local Source

Since you're using local source code, you have two options:

### Option A: Manual Source Copy (Easiest)

1. Copy your project to Jenkins workspace before running pipeline
2. Pipeline will use the code from workspace

### Option B: Use File System SCM

1. In pipeline job configuration:
   - **Pipeline Definition**: **Pipeline script from SCM**
   - **SCM**: Select **File System** (if available) or **None**
   - **Script Path**: `Jenkinsfile`

## Step 7: Run the Pipeline

1. Go to your pipeline job: `student-crud-pipeline`
2. Click **Build Now**
3. Click on the build number to see progress
4. Click **Console Output** to see detailed logs

## Troubleshooting

### Issue: Maven not found
**Solution**: 
- Verify Maven is configured in Global Tool Configuration
- Check that Maven-3.9 name matches in Jenkinsfile

### Issue: JDK not found
**Solution**:
- Verify JDK-17 is configured in Global Tool Configuration
- Ensure JAVA_HOME is set correctly

### Issue: Docker command not found
**Solution**:
- Ensure Docker is installed and running
- For Windows: Check Docker Desktop is running
- For Docker Jenkins: Ensure Docker socket is mounted

### Issue: Permission denied for Docker
**Solution**:
- Add Jenkins user to Docker group
- Or run Jenkins with appropriate permissions

### Issue: Source code not found
**Solution**:
- Ensure project files are in Jenkins workspace
- Check workspace path in Jenkins job configuration
- Verify file paths in Jenkinsfile match your structure

### Issue: Tests failing
**Solution**:
- Check test output in Console Output
- Verify all dependencies are available
- Check Java version compatibility

### Issue: Docker build fails
**Solution**:
- Ensure JAR file exists in `target/` directory
- Check Dockerfile path is correct
- Verify Docker daemon is running

## Verifying Deployment

After successful pipeline run:

1. **Check Docker containers:**
   ```bash
   docker ps
   ```
   Should show `student-crud-app` container running

2. **Check application health:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```
   Or open in browser: `http://localhost:8080/actuator/health`

3. **Access application:**
   - API: `http://localhost:8080/api/students`
   - H2 Console: `http://localhost:8080/h2-console`

## Next Steps

- Set up webhooks for automatic builds (if using Git later)
- Configure email notifications for build results
- Set up build schedules if needed
- Add additional stages as needed

## Notes

- Docker images are kept locally (not pushed to registry)
- Source code is used from local workspace
- All artifacts (JAR files, test reports) are archived in Jenkins
- Pipeline runs deployment automatically on every build

