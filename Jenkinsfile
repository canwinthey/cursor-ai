pipeline {
    agent any
    
    environment {
        // Application configuration
        APP_NAME = 'student-crud'
        APP_VERSION = "${env.BUILD_NUMBER}"
        DOCKER_IMAGE = "${APP_NAME}:${APP_VERSION}"
        DOCKER_IMAGE_LATEST = "${APP_NAME}:latest"
        
        // Docker registry (configure as needed)
        DOCKER_REGISTRY = credentials('docker-registry-url') ?: ''
        DOCKER_REGISTRY_CREDENTIALS = credentials('docker-registry-credentials') ?: ''
        
        // Maven configuration
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=256m'
        
        // SonarQube configuration (optional - configure in Jenkins)
        SONAR_TOKEN = credentials('sonar-token') ?: ''
    }
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }
    
    options {
        // Keep last 10 builds
        buildDiscarder(logRotator(numToKeepStr: '10'))
        
        // Timeout after 30 minutes
        timeout(time: 30, unit: 'MINUTES')
        
        // Add timestamps to console output
        timestamps()
        
        // Archive test results
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    // For local deployment, source code is already in workspace
                    // If using SCM, uncomment the checkout line below
                    echo "Using source code from workspace"
                    // checkout scm  // Uncomment if using Git/SCM
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo "Building application with Maven..."
                    sh """
                        mvn clean compile -DskipTests
                    """
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    echo "Running tests..."
                    sh """
                        mvn test
                    """
                }
            }
            post {
                always {
                    // Publish test results
                    junit 'target/surefire-reports/*.xml'
                    
                    // Publish JaCoCo code coverage report
                    publishCoverage adapters: [
                        jacocoAdapter('target/site/jacoco/jacoco.xml')
                    ], sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
                }
            }
        }
        
        stage('Code Coverage Check') {
            steps {
                script {
                    echo "Checking code coverage (minimum 80%)..."
                    sh """
                        mvn jacoco:check
                    """
                }
            }
        }
        
        stage('Package') {
            steps {
                script {
                    echo "Packaging application..."
                    sh """
                        mvn package -DskipTests
                    """
                }
            }
            post {
                success {
                    echo "JAR file created successfully"
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('SonarQube Analysis') {
            when {
                expression { 
                    return env.SONAR_TOKEN != null && env.SONAR_TOKEN != '' 
                }
            }
            steps {
                script {
                    echo "Running SonarQube analysis..."
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                                -Dsonar.projectKey=${APP_NAME} \
                                -Dsonar.host.url=\${SONAR_HOST_URL} \
                                -Dsonar.login=\${SONAR_TOKEN}
                        """
                    }
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image: ${DOCKER_IMAGE}"
                    sh """
                        docker build -t ${DOCKER_IMAGE} -t ${DOCKER_IMAGE_LATEST} .
                    """
                }
            }
        }
        
        stage('Docker Image Security Scan') {
            when {
                expression {
                    // Only run if Trivy or similar tool is available
                    return sh(script: 'which trivy || which docker-scout', returnStatus: true) == 0
                }
            }
            steps {
                script {
                    echo "Scanning Docker image for vulnerabilities..."
                    sh """
                        # Try Trivy first, fallback to docker scout
                        if command -v trivy &> /dev/null; then
                            trivy image --exit-code 0 --severity HIGH,CRITICAL ${DOCKER_IMAGE}
                        elif docker scout cves ${DOCKER_IMAGE}; then
                            docker scout cves ${DOCKER_IMAGE}
                        fi
                    """
                }
            }
        }
        
        stage('Push Docker Image') {
            when {
                expression {
                    // Only push if Docker registry is configured (optional for local deployment)
                    return env.DOCKER_REGISTRY != null && env.DOCKER_REGISTRY != ''
                }
            }
            steps {
                script {
                    echo "Pushing Docker image to registry..."
                    withCredentials([usernamePassword(credentialsId: 'docker-registry-credentials', 
                                                     usernameVariable: 'DOCKER_USER', 
                                                     passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            docker login -u \${DOCKER_USER} -p \${DOCKER_PASS} ${DOCKER_REGISTRY}
                            docker tag ${DOCKER_IMAGE} ${DOCKER_REGISTRY}/${DOCKER_IMAGE}
                            docker tag ${DOCKER_IMAGE_LATEST} ${DOCKER_REGISTRY}/${DOCKER_IMAGE_LATEST}
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_LATEST}
                        """
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    echo "Deploying application using Docker Compose..."
                    sh """
                        # Stop and remove existing containers
                        docker-compose down || true
                        
                        # Start new containers
                        docker-compose up -d --build
                        
                        # Wait for health check
                        echo "Waiting for application to be healthy..."
                        sleep 10
                        
                        # Check if application is running
                        docker-compose ps
                    """
                }
            }
            post {
                success {
                    echo "Application deployed successfully!"
                    echo "Access the application at: http://localhost:9090"
                }
                failure {
                    echo "Deployment failed. Check logs:"
                    sh "docker-compose logs --tail=50"
                }
            }
        }
    }
    
    post {
        always {
            // Clean up Docker images to save space (optional)
            // Commented out for local deployment to keep images
            // script {
            //     sh """
            //         docker rmi ${DOCKER_IMAGE} || true
            //         docker system prune -f || true
            //     """
            // }
            
            // Clean workspace (commented for local deployment to preserve artifacts)
            // cleanWs()
        }
        
        success {
            echo "Pipeline completed successfully!"
            // You can add notifications here (email, Slack, etc.)
        }
        
        failure {
            echo "Pipeline failed!"
            // You can add failure notifications here
        }
        
        unstable {
            echo "Pipeline is unstable (tests failed or coverage below threshold)"
        }
    }
}

