package com.cursor.product.service.impl;

import com.cursor.product.domain.ProductDto;
import com.cursor.product.exception.ResourceNotFoundException;
import com.cursor.product.mapper.ProductMapper;
import com.cursor.product.model.Product;
import com.cursor.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Test Description", new BigDecimal("99.99"));
        productDto = new ProductDto(1L, "Test Product", "Test Description", new BigDecimal("99.99"));
    }

    @Test
    void testCreateProduct() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.createProduct(productDto);

        assertNotNull(result);
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getName(), result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(productDto.getId(), result.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product, new Product(2L, "Product 2", "Desc 2", new BigDecimal("199.99")));
        List<ProductDto> productDtos = Arrays.asList(productDto, new ProductDto(2L, "Product 2", "Desc 2", new BigDecimal("199.99")));

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return productDtos.stream()
                    .filter(dto -> dto.getId().equals(p.getId()))
                    .findFirst()
                    .orElse(null);
        });

        List<ProductDto> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDto updatedDto = new ProductDto(1L, "Updated Product", "Updated Description", new BigDecimal("149.99"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(updatedDto);

        ProductDto result = productService.updateProduct(1L, updatedDto);

        assertNotNull(result);
        verify(productMapper, times(1)).updateEntity(updatedDto, product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_NotFound() {
        ProductDto updatedDto = new ProductDto(1L, "Updated Product", "Updated Description", new BigDecimal("149.99"));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, updatedDto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any());
    }

    @Test
    void testGetAllProducts_EmptyList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        List<ProductDto> result = productService.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testCreateProduct_WithNullId() {
        ProductDto dtoWithoutId = new ProductDto(null, "New Product", "Description", new BigDecimal("50.00"));
        Product productWithoutId = new Product(null, "New Product", "Description", new BigDecimal("50.00"));
        Product savedProduct = new Product(1L, "New Product", "Description", new BigDecimal("50.00"));
        ProductDto savedDto = new ProductDto(1L, "New Product", "Description", new BigDecimal("50.00"));

        when(productMapper.toEntity(dtoWithoutId)).thenReturn(productWithoutId);
        when(productRepository.save(productWithoutId)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(savedDto);

        ProductDto result = productService.createProduct(dtoWithoutId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).save(productWithoutId);
    }
}

