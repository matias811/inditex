package com.inditex.core.platform.ecommerce.app.domain.service;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import com.inditex.core.platform.ecommerce.app.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = Product.builder()
                .id(1L)
                .brandId(1)
                .productId(35455)
                .priceList(1)
                .priority(0)
                .price(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .build();
    }

    @Test
    public void testGetApplicablePrice_found() {
        when(productRepository.findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00")))
                .thenReturn(Collections.singletonList(product));

        Product result = productService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00"));

        assertNotNull(result);
        assertEquals(product.getPrice(), result.getPrice());
        verify(productRepository, times(1)).findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00"));
    }

    @Test
    public void testGetApplicablePrice_notFound() {
        when(productRepository.findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00")))
                .thenReturn(Collections.emptyList());

        Product result = productService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00"));

        assertNull(result);
        verify(productRepository, times(1)).findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00"));
    }

    @Test
    void saveProduct_shouldSaveProduct() {
        Product product = Product.builder()
                .id(2L)
                .brandId(2)
                .productId(35460)
                .priceList(1)
                .priority(0)
                .price(BigDecimal.valueOf(40.50))
                .currency("EUR")
                .startDate(LocalDateTime.parse("2020-10-14T00:00:00"))
                .endDate(LocalDateTime.parse("2021-12-31T23:59:59"))
                .build();

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals(2, savedProduct.getId());
    }

    @Test
    void deleteProduct_shouldDeleteProductIfExists() {
        Product product = Product.builder()
                .id(1L)
                .brandId(2)
                .productId(37055)
                .priceList(1)
                .priority(0)
                .price(BigDecimal.valueOf(37.50))
                .currency("EUR")
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .build();

        productRepository.deleteById(product.getId());

        verify(productRepository, times(1)).deleteById(product.getId());
    }
}
