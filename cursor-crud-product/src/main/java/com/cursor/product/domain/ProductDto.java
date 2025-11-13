package com.cursor.product.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for Product entity.
 * <p>
 * This class represents the product data structure used for API requests and responses.
 * It includes validation annotations to ensure data integrity.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    /**
     * Unique identifier for the product.
     */
    private Long id;

    /**
     * Name of the product.
     * Must not be null or blank.
     */
    @NotBlank(message = "Product name is required")
    @NotNull(message = "Product name cannot be null")
    private String name;

    /**
     * Description of the product.
     * Must not be null or blank.
     */
    @NotBlank(message = "Product description is required")
    @NotNull(message = "Product description cannot be null")
    private String description;

    /**
     * Price of the product.
     * Must not be null and must be greater than 0.01.
     */
    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private BigDecimal price;
}

