package com.cursor.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Product entity class representing a product in the database.
 * <p>
 * This is a JPA entity that maps to the "products" table in the database.
 * It contains product information including id, name, description, and price.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * Unique identifier for the product.
     * Auto-generated using IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the product.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Description of the product.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Price of the product.
     * Stored with precision 19 and scale 2 (e.g., 9999999999999999.99).
     * Cannot be null.
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
}

