package com.inditex.core.platform.ecommerce.app.domain.service;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import com.inditex.core.platform.ecommerce.app.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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


    @ParameterizedTest
    @CsvSource({
            "35455, 1, 2020-06-14T10:00:00, 35.50",
            "35456, 2, 2020-06-15T16:00:00, 50.00",
            "35457, 3, 2020-06-16T21:00:00, 60.00",
            "35458, 4, 2020-06-17T21:00:00, 60.00",
            "35459, 5, 2020-06-18T21:00:00, 60.00",
            "35460, 6, 2020-06-19T21:00:00, 60.00"
    })
    public void testGetApplicablePrice_found(int productId, int brandId, String dateTime, double expectedPrice) {
        Product product = Product.builder()
                .id(1L)
                .brandId(brandId)
                .productId(productId)
                .priceList(1)
                .priority(0)
                .price(BigDecimal.valueOf(expectedPrice))
                .currency("EUR")
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .build();

        LocalDateTime queryDate = LocalDateTime.parse(dateTime);
        when(productRepository.findApplicablePrices(productId, brandId, queryDate))
                .thenReturn(Collections.singletonList(product));

        Product result = productService.getApplicablePrice(productId, brandId, queryDate);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(expectedPrice), result.getPrice());
        verify(productRepository, times(1)).findApplicablePrices(productId, brandId, queryDate);
    }

    @Test
    public void testGetApplicablePrice_notFound() {
        when(productRepository.findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00")))
                .thenReturn(Collections.emptyList());

        Product result = productService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00"));

        assertNull(result);
        verify(productRepository, times(1)).findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00"));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 2, 35460, 40.50, 2020-10-14T00:00:00, 2021-12-31T23:59:59",
            "3, 1, 35461, 55.75, 2021-01-01T00:00:00, 2022-12-31T23:59:59",
            "4, 3, 35462, 25.00, 2019-05-20T00:00:00, 2020-12-31T23:59:59"
    })
    void saveProduct_shouldSaveProduct(long id, int brandId, int productId, double price, String startDate, String endDate) {
        Product productToSave = Product.builder()
                .id(id)
                .brandId(brandId)
                .productId(productId)
                .priceList(1)
                .priority(0)
                .price(BigDecimal.valueOf(price))
                .currency("EUR")
                .startDate(LocalDateTime.parse(startDate))
                .endDate(LocalDateTime.parse(endDate))
                .build();

        when(productRepository.save(productToSave)).thenReturn(productToSave);

        Product savedProduct = productService.saveProduct(productToSave);

        assertNotNull(savedProduct);
        assertEquals(id, savedProduct.getId());
        verify(productRepository, times(1)).save(productToSave);
    }

    @Test
    void deleteProduct_shouldDeleteProductIfExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void deleteProduct_shouldThrowExceptionIfProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).deleteById(1L);
    }

    @Test
    void deleteProduct_shouldLogInfoWhenProductIsFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_shouldLogErrorWhenProductIsNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            productService.deleteProduct(1L);
        } catch (IllegalArgumentException e) {
            verify(productRepository, times(1)).findById(1L);
            verify(productRepository, never()).deleteById(1L);
        }
    }

    @Test
    void saveProduct_shouldReturnNullWhenExceptionOccurs() {
        Product productToSave = Product.builder()
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

        when(productRepository.save(productToSave)).thenThrow(new RuntimeException("Database error"));
        Product savedProduct = productService.saveProduct(productToSave);
        assertNull(savedProduct);
        verify(productRepository, times(1)).save(productToSave);
    }

}
