package com.cursor.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Product CRUD service.
 * <p>
 * This is a Spring Boot application that provides REST APIs for managing products.
 * The application runs on port 9090 and uses H2 in-memory database.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootApplication
public class ProductApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}

