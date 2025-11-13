Act as java, spring-boot expert.
1. Create a java 17, spring-boot 3.4, maven project.
2. The project must have CRUD operation for Product.

3. Basic fields (id, name, email, age)
4. Databsase: H2
5. REST API with standard endpoints

6. Serverside validations
7. Lombok

Add StrudentDto in domain folder and add serverside validations.
Add student.sql file in resources folder. Add 5 sample insert queries in this file.
Add jacoco, maven plugin, junit 5 test cases
Check for any sonarQube vulnerabilities
Dockerize this application

In the dockefile remove below command
RUN mvn clean package -DskipTests
I don't want the build to happen in docker.
Create the build outside of @Dockerfile and then copy the jar file in @Dockerfile
http://localhost:9090/actuator/health url is not working. plz check.
There are some errors seen on the console. Can you check and fix them.
Stop the application and run it in docker container
Stop the container

Following the above 5 Next Steps.
Deploy the application to jenkins.
Note: My source-code is not present in Github and docker image is not present in docker hub. Both are in local environment.
Follow the steps and deploy the application. Once done open the jenkins UI in chrome browser

There is conflict between the jenkins port and application port. Both are 8080. Change the student application port to 9090 and restart the jenkins pipleline

API: http://localhost:9090/api/students
Health check: http://localhost:9090/actuator/health
H2 Console: http://localhost:9090/h2-console

mvn spring-boot:run to start the application. The API will be available at http://localhost:8080/api/students.

===============================
gemini
===============================
Act as java, spring-boot expert.
1. Create a java 17, spring-boot 3.2 maven project.
2. The project must have CRUD operation for Product.
3. Endpoint:  /api/product/

Package name: com.gemini.product
Port number: 9090
Fields: id, name, description, price
Folders: controller, domain, service, model, mapper, repository, config, exception
Files: Create files as per the folder structure
Create ProductDto and Product (Entity) separately.
Validation: serverside validations.
Dependency: Lombok, validation, actuator, swagger, Junit 5
Plugin: sonar, jacoco
Database: H2
Add data.sql file in resources folder. Add 5 sample insert queries in this file.
Update data.sql with products like Shirt, Trouser, Shoes, TV, Washing Machine etc
UnitTestCases: junit 5 test cases for all classes with coverage > 80%
ExceptionHandling: Create Global Exceptionhandler
CORS: Add CORS configuration
Swagger: Create Swagger Sandbox

CI/CD:
1. Docker:
- Dockerize this application. Keep the build (mvn clean package) outside of dockerfile
- Build the docker image and name it as gemini-crud-product

2. Jenkins Pipeline
- Create jenkins multistage pipeline for this project.
- In Stage: 'Build Docker Image' don't push the docker image to dockerhub. Keep it in local.
- In Stage: 'Deploy' Deploy the application by picking the docker image from local.
- Store junit 5 coverage report on Jenkins server. Provide me the url at the end.


Jenkins Instructions:
Deploy this application to Jenkins locally.
Part 1: Run Jenkins in a Docker Container
1. Create a Docker volume: docker volume create jenkins_home
2. Run Jenkins container: docker run -d -p 8080:8080 -p 50000:50000 --name jenkins-server -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts
3. Retrieve admin password: docker logs jenkins-server
4. Access UI: Go to http://localhost:8080 and complete setup.

docker run -d -v //var/run/docker.sock:/var/run/docker.sock
-v jenkins_home:/var/jenkins_home
-p 8080:8080 -p 50000:50000  
--user root --name Jenkins jenkins/jenkins:lts

Install Docker CLI inside existing Jenkins container

docker exec -u root -it Jenkins bash
apt-get update
apt-get install -y docker.io

cd /var/jenkins_home/workspace
rm -rf product-crud*
mkdir product-crud
chown -R jenkins:jenkins product-crud

git config --global --add safe.directory /var/jenkins_home/workspace/gemini-crud-product

mvn clean verify sonar:sonar \
-Dsonar.projectKey=gemini-crud-product \
-Dsonar.host.url=http://localhost:9000 \
-Dsonar.login=squ_bc0024e2e030cdfb2997d364f01c6be48c12cce4
