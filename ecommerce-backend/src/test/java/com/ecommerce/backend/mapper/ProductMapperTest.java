package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.request.ProductRequest;
import com.ecommerce.backend.dto.response.ProductResponse;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.enums.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testProductRequestToEntity() {
        // Given
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setSku("TEST-001");
        request.setPrice(BigDecimal.valueOf(99.99));
        request.setStockQuantity(10);
        request.setSlug("test-product");
        request.setCategoryId(1L);

        // When
        Product product = productMapper.toEntity(request);

        // Then
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals("TEST-001", product.getSku());
        assertEquals(BigDecimal.valueOf(99.99), product.getPrice());
        assertEquals(10, product.getStockQuantity());
        assertEquals("test-product", product.getSlug());
        assertNotNull(product.getCategory());
        assertEquals(1L, product.getCategory().getId());
    }

    @Test
    void testProductToResponse() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("Test Category")
                .slug("test-category")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .sku("TEST-001")
                .price(BigDecimal.valueOf(99.99))
                .stockQuantity(10)
                .status(ProductStatus.ACTIVE)
                .category(category)
                .build();

        // When
        ProductResponse response = productMapper.toResponse(product);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals("TEST-001", response.getSku());
        assertEquals(BigDecimal.valueOf(99.99), response.getPrice());
        assertEquals(10, response.getStockQuantity());
        assertEquals(ProductStatus.ACTIVE, response.getStatus());
        assertTrue(response.getIsInStock());
        assertTrue(response.getIsAvailable());
    }
}