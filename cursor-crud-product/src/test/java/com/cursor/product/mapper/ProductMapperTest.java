package com.cursor.product.mapper;

import com.cursor.product.domain.ProductDto;
import com.cursor.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Test Description", new BigDecimal("99.99"));
        productDto = new ProductDto(1L, "Test Product", "Test Description", new BigDecimal("99.99"));
    }

    @Test
    void testToDto() {
        ProductDto result = productMapper.toDto(product);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
    }

    @Test
    void testToEntity() {
        Product result = productMapper.toEntity(productDto);

        assertNotNull(result);
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getDescription(), result.getDescription());
        assertEquals(productDto.getPrice(), result.getPrice());
    }

    @Test
    void testUpdateEntity() {
        Product existingProduct = new Product(1L, "Old Name", "Old Description", new BigDecimal("50.00"));
        ProductDto updatedDto = new ProductDto(null, "New Name", "New Description", new BigDecimal("100.00"));

        productMapper.updateEntity(updatedDto, existingProduct);

        assertEquals("New Name", existingProduct.getName());
        assertEquals("New Description", existingProduct.getDescription());
        assertEquals(new BigDecimal("100.00"), existingProduct.getPrice());
        assertEquals(1L, existingProduct.getId()); // ID should remain unchanged
    }

    @Test
    void testUpdateEntityWithNullValues() {
        Product existingProduct = new Product(1L, "Existing Name", "Existing Description", new BigDecimal("50.00"));
        ProductDto updatedDto = new ProductDto(null, null, null, null);

        productMapper.updateEntity(updatedDto, existingProduct);

        // With NullValuePropertyMappingStrategy.IGNORE, null values should not update
        assertEquals("Existing Name", existingProduct.getName());
        assertEquals("Existing Description", existingProduct.getDescription());
        assertEquals(new BigDecimal("50.00"), existingProduct.getPrice());
    }

    @Test
    void testToDtoWithNullProduct() {
        ProductDto result = productMapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToEntityWithNullDto() {
        Product result = productMapper.toEntity(null);
        assertNull(result);
    }
}

