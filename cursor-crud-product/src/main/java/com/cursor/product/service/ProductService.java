package com.cursor.product.service;

import com.cursor.product.domain.ProductDto;

import java.util.List;

/**
 * Service interface for Product business logic operations.
 * <p>
 * This interface defines the contract for all product-related business operations
 * including CRUD operations.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param productDto the product data transfer object containing product information
     * @return the created product as a DTO
     */
    ProductDto createProduct(ProductDto productDto);

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return the product DTO if found
     * @throws com.cursor.product.exception.ResourceNotFoundException if product is not found
     */
    ProductDto getProductById(Long id);

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all product DTOs
     */
    List<ProductDto> getAllProducts();

    /**
     * Updates an existing product.
     *
     * @param id the unique identifier of the product to update
     * @param productDto the product DTO containing updated information
     * @return the updated product as a DTO
     * @throws com.cursor.product.exception.ResourceNotFoundException if product is not found
     */
    ProductDto updateProduct(Long id, ProductDto productDto);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to delete
     * @throws com.cursor.product.exception.ResourceNotFoundException if product is not found
     */
    void deleteProduct(Long id);
}

