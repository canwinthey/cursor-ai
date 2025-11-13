package com.cursor.product.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoTest {

    private Validator validator;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        productDto = new ProductDto();
    }

    @Test
    void testValidProductDto() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("99.99"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullName() {
        productDto.setId(1L);
        productDto.setName(null);
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("99.99"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertEquals(2, violations.size()); // @NotNull and @NotBlank both fail
    }

    @Test
    void testBlankName() {
        productDto.setId(1L);
        productDto.setName("");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("99.99"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("name")));
    }

    @Test
    void testNullDescription() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription(null);
        productDto.setPrice(new BigDecimal("99.99"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertEquals(2, violations.size()); // @NotNull and @NotBlank both fail
    }

    @Test
    void testBlankDescription() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("");
        productDto.setPrice(new BigDecimal("99.99"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("description")));
    }

    @Test
    void testNullPrice() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(null);

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("price")));
    }

    @Test
    void testNegativePrice() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("-1.00"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("greater than 0")));
    }

    @Test
    void testZeroPrice() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(BigDecimal.ZERO);

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("greater than 0")));
    }

    @Test
    void testPriceAtMinimum() {
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("0.01"));

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        productDto.setId(1L);
        productDto.setName("Product Name");
        productDto.setDescription("Product Description");
        productDto.setPrice(new BigDecimal("50.00"));

        assertEquals(1L, productDto.getId());
        assertEquals("Product Name", productDto.getName());
        assertEquals("Product Description", productDto.getDescription());
        assertEquals(new BigDecimal("50.00"), productDto.getPrice());
    }

    @Test
    void testAllArgsConstructor() {
        ProductDto dto = new ProductDto(1L, "Test", "Desc", new BigDecimal("99.99"));

        assertEquals(1L, dto.getId());
        assertEquals("Test", dto.getName());
        assertEquals("Desc", dto.getDescription());
        assertEquals(new BigDecimal("99.99"), dto.getPrice());
    }

    @Test
    void testNoArgsConstructor() {
        ProductDto dto = new ProductDto();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getPrice());
    }
}

