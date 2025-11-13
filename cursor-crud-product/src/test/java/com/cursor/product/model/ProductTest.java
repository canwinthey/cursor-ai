package com.cursor.product.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void testGettersAndSetters() {
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));

        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
    }

    @Test
    void testAllArgsConstructor() {
        Product product = new Product(1L, "Test", "Desc", new BigDecimal("50.00"));

        assertEquals(1L, product.getId());
        assertEquals("Test", product.getName());
        assertEquals("Desc", product.getDescription());
        assertEquals(new BigDecimal("50.00"), product.getPrice());
    }

    @Test
    void testNoArgsConstructor() {
        Product product = new Product();

        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getDescription());
        assertNull(product.getPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        Product product1 = new Product(1L, "Product", "Description", new BigDecimal("99.99"));
        Product product2 = new Product(1L, "Product", "Description", new BigDecimal("99.99"));
        Product product3 = new Product(2L, "Product", "Description", new BigDecimal("99.99"));

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testToString() {
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));

        String toString = product.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test Product"));
        assertTrue(toString.contains("99.99"));
    }
}

