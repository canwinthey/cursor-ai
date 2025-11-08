# Jenkins Quick Start Guide

## Quick Setup Checklist

- [ ] **Step 1**: Install Jenkins (see JENKINS-SETUP.md)
- [ ] **Step 2**: Install required plugins (Pipeline, Maven, Docker, JUnit, JaCoCo)
- [ ] **Step 3**: Configure Maven-3.9 and JDK-17 in Global Tool Configuration
- [ ] **Step 4**: Ensure Docker is running and accessible
- [ ] **Step 5**: Create pipeline job and configure

## Fastest Setup Method

### 1. Copy Project to Jenkins Workspace

**Option A: Use PowerShell Script**
```powershell
.\setup-jenkins-workspace.ps1
```

**Option B: Manual Copy**
```powershell
# Find your Jenkins workspace (usually)
$workspace = "C:\Program Files\Jenkins\workspace\student-crud-pipeline"

# Create directory
New-Item -ItemType Directory -Path $workspace -Force

# Copy project (adjust source path)
Copy-Item -Path "C:\cursor-ai\basic-sb-proj\*" -Destination $workspace -Recurse -Force
```

### 2. Create Jenkins Pipeline Job

1. Open Jenkins: `http://localhost:8080`
2. Click **New Item**
3. Name: `student-crud-pipeline`
4. Type: **Pipeline** → Click **OK**
5. Scroll to **Pipeline** section
6. **Definition**: Select **Pipeline script**
7. **Script**: Open your `Jenkinsfile` and copy ALL contents into the text area
8. Click **Save**

### 3. Run Pipeline

1. Click **Build Now** on the job page
2. Watch the build progress
3. Check **Console Output** for details

## Expected Pipeline Stages

When you run the pipeline, you should see these stages:

1. ✅ Checkout - Uses local workspace
2. ✅ Build - Compiles with Maven
3. ✅ Test - Runs unit tests
4. ✅ Code Coverage Check - Validates 80% coverage
5. ✅ Package - Creates JAR file
6. ⏭️ SonarQube Analysis - Skipped (optional)
7. ✅ Build Docker Image - Builds Docker image
8. ⏭️ Docker Security Scan - Skipped if tools not available
9. ⏭️ Push Docker Image - Skipped (no registry configured)
10. ✅ Deploy - Deploys with Docker Compose

## Verify Deployment

After successful pipeline:

```powershell
# Check if container is running
docker ps

# Test application
curl http://localhost:8080/actuator/health

# Or open in browser
# http://localhost:8080/api/students
```

## Common Issues & Quick Fixes

| Issue | Quick Fix |
|-------|-----------|
| Maven not found | Check Global Tool Configuration → Maven-3.9 |
| JDK not found | Check Global Tool Configuration → JDK-17 |
| Docker not found | Ensure Docker Desktop is running |
| Permission denied | Run Jenkins as Administrator or configure Docker access |
| Source not found | Copy project to Jenkins workspace |
| Build fails | Check Console Output for specific error |

## Pipeline Configuration Summary

- **Source**: Local workspace (no Git required)
- **Build Tool**: Maven 3.9
- **Java Version**: 17
- **Docker**: Local images (no registry push)
- **Deployment**: Docker Compose
- **Artifacts**: JAR files archived in Jenkins

## Next Steps After Setup

1. ✅ Test the pipeline with a manual build
2. ✅ Verify application is accessible
3. ✅ Check test reports and coverage
4. ✅ Review archived artifacts
5. ⏭️ (Optional) Configure email notifications
6. ⏭️ (Optional) Set up build schedules

## Need Help?

- See `JENKINS-SETUP.md` for detailed setup instructions
- Check Jenkins Console Output for error details
- Verify all prerequisites are installed

