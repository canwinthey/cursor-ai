# Product CRUD Application - Implementation Plan

## Project Overview

A complete Spring Boot REST API application for Product CRUD operations with multiple UI/UX design options, comprehensive testing, Docker containerization, and Jenkins CI/CD pipeline.

**Package Name:** `com.cursor.product`  
**Port:** 9090  
**Base Endpoint:** `/api/product`  
**Database:** H2 (In-memory)

---

## 1. Project Structure

### Package Organization
```
com.cursor.product/
├── controller/     - REST controllers
├── domain/         - DTO classes
├── model/          - Entity classes
├── service/        - Business logic
│   └── impl/       - Service implementations
├── repository/     - Data access layer
├── mapper/         - DTO-Entity mapping (MapStruct)
├── config/         - Configuration classes
└── exception/      - Exception handling
```

### Key Files
- **Main Application:** `ProductApplication.java`
- **Entity:** `model/Product.java` (id, name, description, price)
- **DTO:** `domain/ProductDto.java` (with validation)
- **Repository:** `repository/ProductRepository.java`
- **Service:** `service/ProductService.java` & `service/impl/ProductServiceImpl.java`
- **Controller:** `controller/ProductController.java`
- **Mapper:** `mapper/ProductMapper.java` (MapStruct)
- **Exception Handler:** `exception/GlobalExceptionHandler.java`
- **Config:** `config/SwaggerConfig.java`, `config/CorsConfig.java`

---

## 2. Dependencies & Plugins

### Maven Dependencies
- **Spring Boot Web** - REST API support
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database
- **Validation** - Server-side validation
- **Actuator** - Monitoring endpoints
- **SpringDoc OpenAPI** - Swagger/OpenAPI documentation
- **Lombok** - Code generation
- **MapStruct** - Entity-DTO mapping
- **JUnit 5** - Testing framework

### Maven Plugins
- **SonarQube Maven Plugin** - Code quality analysis
- **JaCoCo Maven Plugin** - Code coverage reporting
- **Spring Boot Maven Plugin** - Application packaging

---

## 3. REST API Endpoints

### Base Path: `/api/product`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/product` | Get all products |
| GET | `/api/product/{id}` | Get product by ID |
| POST | `/api/product` | Create new product |
| PUT | `/api/product/{id}` | Update product |
| DELETE | `/api/product/{id}` | Delete product |

### Request/Response Format
- **Request Body:** JSON (ProductDto)
- **Response:** JSON (ProductDto or List<ProductDto>)
- **Validation:** Server-side validation with Jakarta Validation

---

## 4. Database Configuration

### H2 Database Settings
- **URL:** `jdbc:h2:mem:productdb`
- **Console:** Enabled at `/h2-console`
- **DDL Mode:** `update` (auto-create tables)
- **Data Initialization:** `data.sql` with 5 sample products

### Sample Data
- Shirt ($29.99)
- Trouser ($49.99)
- Shoes ($79.99)
- TV ($599.99)
- Washing Machine ($449.99)

---

## 5. Validation & Exception Handling

### Server-Side Validation
- **ProductDto** includes validation annotations:
  - `@NotNull` - Required fields
  - `@NotBlank` - Non-empty strings
  - `@DecimalMin` - Price must be > 0.01

### Global Exception Handler
- **ResourceNotFoundException** - 404 Not Found
- **MethodArgumentNotValidException** - 400 Bad Request (validation errors)
- **ConstraintViolationException** - 400 Bad Request (constraint violations)
- **Exception** - 500 Internal Server Error

### Error Response Format
```json
{
  "timestamp": "2025-11-12T22:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/product",
  "validationErrors": ["name: Product name is required"]
}
```

---

## 6. Documentation & API Testing

### Swagger/OpenAPI
- **Swagger UI:** `http://localhost:9090/swagger-ui.html`
- **API Docs:** `http://localhost:9090/api-docs`
- **Configuration:** `SwaggerConfig.java`
- **Features:** Interactive API testing, request/response examples

### JavaDocs
- Comprehensive JavaDoc comments on all classes and methods
- Includes parameter descriptions, return values, and exceptions
- Follows JavaDoc standards

---

## 7. CORS Configuration

### CORS Settings
- **Allowed Origins:** All (`*`)
- **Allowed Methods:** GET, POST, PUT, DELETE, OPTIONS, PATCH
- **Allowed Headers:** All
- **Credentials:** Disabled (set to false)
- **Max Age:** 3600 seconds
- **Configuration:** `CorsConfig.java`

---

## 8. Testing

### Test Coverage Requirements
- **Target:** > 80% code coverage
- **Framework:** JUnit 5
- **Mocking:** Mockito

### Test Files
- `ProductServiceImplTest.java` - Service layer unit tests
- `ProductControllerTest.java` - Controller integration tests
- `GlobalExceptionHandlerTest.java` - Exception handler tests
- `ProductMapperTest.java` - Mapper tests
- `ProductDtoTest.java` - DTO validation tests
- `ProductTest.java` - Entity tests
- `ErrorResponseTest.java` - Error response tests
- `ResourceNotFoundExceptionTest.java` - Exception tests
- `SwaggerConfigTest.java` - Configuration tests
- `CorsConfigTest.java` - CORS configuration tests

### Test Execution
```bash
mvn test
mvn jacoco:report  # Generate coverage report
```

---

## 9. Docker Configuration

### Dockerfile
- **Base Image:** `eclipse-temurin:17-jre-jammy`
- **Build Process:** Maven build kept outside Dockerfile
- **Image Name:** `cursor-crud-product`
- **Port:** 9090 (exposed)
- **JVM Options:** `-Xmx512m -Xms256m`

### Build Commands
```bash
# Build Maven project
mvn clean package

# Build Docker image
docker build -t cursor-crud-product:latest .

# Run container
docker run -d -p 9090:9090 --name cursor-crud-product-container cursor-crud-product:latest
```

### .dockerignore
- Excludes unnecessary files (IDE, logs, test files, etc.)
- Optimizes Docker build context

---

## 10. Jenkins CI/CD Pipeline

### Pipeline Stages

1. **Checkout** - Check out code from repository
2. **Build** - Compile and package (`mvn clean package -DskipTests`)
3. **Test** - Run JUnit 5 tests (`mvn test`)
4. **Generate Coverage Report** - Generate JaCoCo report (`mvn jacoco:report`)
5. **Build Docker Image** - Build Docker image locally (NOT pushed to DockerHub)
6. **Deploy** - Deploy using local Docker image

### Pipeline Features
- **Test Results:** Published automatically
- **Coverage Reports:** Stored on Jenkins server
- **Docker Image:** Built and stored locally
- **Deployment:** Automatic container deployment

### Coverage Report URL
```
http://<jenkins-server-url>/job/<job-name>/<build-number>/HTML_Report/
```

### Jenkinsfile Location
- Root directory: `Jenkinsfile`

---

## 11. UI/UX Design Options

### Overview
5 distinct UI/UX design implementations for the frontend, each with unique color schemes, typography, and styling.

### Option 1: Modern Minimalist
- **File:** `design-option1.html`
- **Colors:** Blue (#2563EB), Green (#10B981)
- **Style:** Clean, professional, corporate-friendly
- **Layout:** Card grid (3 columns desktop)

### Option 2: Vibrant E-Commerce
- **File:** `design-option2.html`
- **Colors:** Orange (#FF6B35), Deep Blue (#004E89)
- **Style:** Bold, engaging, energetic
- **Layout:** Product showcase grid (4 columns desktop)

### Option 3: Elegant Dark Theme
- **File:** `design-option3.html`
- **Colors:** Purple (#8B5CF6), Cyan (#06B6D4)
- **Style:** Sophisticated, modern, premium
- **Layout:** Dark cards with glow effects

### Option 4: Material Design
- **File:** `design-option4.html`
- **Colors:** Material Blue (#1976D2), Orange (#FF9800)
- **Style:** Google's Material Design language
- **Layout:** Material elevation and ripple effects

### Option 5: Product Dashboard
- **File:** `design-option5.html`
- **Colors:** Indigo (#6366F1), Green (#10B981), Amber (#F59E0B)
- **Style:** Analytics-focused dashboard
- **Features:**
  - Dashboard widgets (Total Products, Most/Least Expensive, Total Value)
  - Data table with sorting and pagination
  - Add Product button
  - Edit and Delete actions per row

### Frontend Structure
```
src/main/resources/static/
├── index.html              # Design selector
├── design-option1.html     # Option 1
├── design-option2.html     # Option 2
├── design-option3.html     # Option 3
├── design-option4.html     # Option 4
├── design-option5.html     # Option 5 (Dashboard)
├── css/
│   ├── main.css
│   ├── option1.css
│   ├── option2.css
│   ├── option3.css
│   ├── option4.css
│   └── option5.css
└── js/
    ├── api.js              # API integration
    ├── app.js              # Application logic
    └── dashboard.js        # Dashboard logic
```

### Frontend Features
- Full CRUD operations
- Responsive design (mobile, tablet, desktop)
- API integration with error handling
- Form validation
- Loading states and notifications
- Real-time updates

---

## 12. Application Properties

### Key Configuration
```properties
# Server
server.port=9090

# H2 Database
spring.datasource.url=jdbc:h2:mem:productdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# Actuator
management.endpoints.web.exposure.include=health,info,metrics

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 13. Access Points

### API Endpoints
- **Base URL:** `http://localhost:9090/api/product`
- **Swagger UI:** `http://localhost:9090/swagger-ui.html`
- **H2 Console:** `http://localhost:9090/h2-console`
- **Actuator:** `http://localhost:9090/actuator`

### Frontend
- **Design Selector:** `http://localhost:9090/index.html`
- **Option 1:** `http://localhost:9090/design-option1.html`
- **Option 2:** `http://localhost:9090/design-option2.html`
- **Option 3:** `http://localhost:9090/design-option3.html`
- **Option 4:** `http://localhost:9090/design-option4.html`
- **Option 5:** `http://localhost:9090/design-option5.html`

---

## 14. Build & Run

### Maven Commands
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn clean package

# Run application
mvn spring-boot:run
```

### Docker Commands
```bash
# Build Docker image
docker build -t cursor-crud-product:latest .

# Run container
docker run -d -p 9090:9090 --name cursor-crud-product-container cursor-crud-product:latest

# Stop container
docker stop cursor-crud-product-container

# Remove container
docker rm cursor-crud-product-container
```

---

## 15. Project Deliverables

### ✅ Completed Features
- [x] Complete CRUD REST API
- [x] H2 database integration
- [x] Server-side validation
- [x] Global exception handling
- [x] Swagger/OpenAPI documentation
- [x] CORS configuration
- [x] JavaDocs for all classes
- [x] Comprehensive test suite (>80% coverage)
- [x] Docker containerization
- [x] Jenkins CI/CD pipeline
- [x] 5 UI/UX design options
- [x] Dashboard with widgets and data table
- [x] Pagination and sorting
- [x] Responsive frontend

### Documentation Files
- `README-FRONTEND.md` - Frontend usage guide
- `README-DOCKER.md` - Docker and CI/CD setup
- `UI-UX-DESIGN-OPTIONS.md` - Design specifications
- `plan.md` - This file

---

## 16. Technical Stack

### Backend
- **Framework:** Spring Boot 3.2.0
- **Java Version:** 17
- **Build Tool:** Maven
- **Database:** H2 (In-memory)
- **ORM:** JPA/Hibernate
- **Validation:** Jakarta Validation
- **API Documentation:** SpringDoc OpenAPI
- **Mapping:** MapStruct

### Frontend
- **Technology:** Vanilla JavaScript, HTML5, CSS3
- **Icons:** Material Icons
- **Fonts:** Google Fonts (Inter, Poppins, Montserrat, Roboto)

### DevOps
- **Containerization:** Docker
- **CI/CD:** Jenkins
- **Code Quality:** SonarQube
- **Coverage:** JaCoCo

---

## 17. Best Practices Implemented

### Code Quality
- Separation of concerns (Controller, Service, Repository)
- DTO pattern for API communication
- MapStruct for type-safe mapping
- Comprehensive exception handling
- Input validation
- JavaDoc documentation

### Testing
- Unit tests for service layer
- Integration tests for controller
- Test coverage > 80%
- Mock-based testing

### Security
- Input validation
- Error message sanitization
- CORS configuration
- SQL injection prevention (JPA)

### Performance
- Transaction management
- Read-only transactions for queries
- Efficient database queries
- Optimized Docker image

---

## 18. Future Enhancements (Optional)

- [ ] Add authentication and authorization
- [ ] Implement caching (Redis)
- [ ] Add product image upload
- [ ] Implement search and filtering
- [ ] Add product categories
- [ ] Export to CSV/Excel
- [ ] Add audit logging
- [ ] Implement rate limiting
- [ ] Add API versioning
- [ ] Implement WebSocket for real-time updates

---

## 19. Troubleshooting

### Common Issues

**Issue:** Primary key violation when adding products
- **Solution:** Removed explicit IDs from `data.sql` to let Hibernate auto-generate

**Issue:** Products added twice
- **Solution:** Removed duplicate form submission handlers

**Issue:** CORS errors
- **Solution:** Verify `CorsConfig.java` is properly configured

**Issue:** Swagger UI not accessible
- **Solution:** Check `springdoc.swagger-ui.path` in `application.properties`

---

## 20. Project Summary

This is a complete, production-ready Spring Boot application with:
- ✅ Full REST API with CRUD operations
- ✅ Comprehensive testing (>80% coverage)
- ✅ Multiple UI/UX design options
- ✅ Docker containerization
- ✅ Jenkins CI/CD pipeline
- ✅ Complete documentation
- ✅ Best practices implementation

The application is ready for deployment and can be extended with additional features as needed.

---

**Last Updated:** November 12, 2025  
**Version:** 1.0.0  
**Status:** ✅ Complete

