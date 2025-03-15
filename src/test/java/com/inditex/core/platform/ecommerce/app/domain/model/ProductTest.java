package com.inditex.core.platform.ecommerce.app.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        product1 = Product.builder()
                .id(1L)
                .brandId(101)
                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 10, 0))
                .priceList(1)
                .productId(1001)
                .priority(1)
                .price(new BigDecimal("199.99"))
                .currency("USD")
                .build();

        product2 = Product.builder()
                .id(1L)
                .brandId(101)
                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 10, 0))
                .priceList(1)
                .productId(1001)
                .priority(1)
                .price(new BigDecimal("199.99"))
                .currency("USD")
                .build();

        product3 = Product.builder()
                .id(2L)
                .brandId(102)
                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 10, 0))
                .priceList(2)
                .productId(1002)
                .priority(2)
                .price(new BigDecimal("299.99"))
                .currency("EUR")
                .build();
    }

    @Test
    void testEquals_SameObject_ShouldReturnTrue() {
        assertTrue(product1.equals(product1));
    }

    @Test
    void testEquals_SameValues_ShouldReturnTrue() {
        assertTrue(product1.equals(product2));
    }

    @Test
    void testEquals_DifferentId_ShouldReturnFalse() {
        product2.setId(3L);
        assertFalse(product1.equals(product2));
    }

    @Test
    void testEquals_DifferentProduct_ShouldReturnFalse() {
        assertFalse(product1.equals(product3));
    }

    @Test
    void testEquals_NullObject_ShouldReturnFalse() {
        assertFalse(product1.equals(null));
    }

    @Test
    void testEquals_DifferentClass_ShouldReturnFalse() {
        assertFalse(product1.equals(new Object()));
    }

    @Test
    void testHashCode_SameValues_ShouldReturnSameHashCode() {
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues_ShouldReturnDifferentHashCode() {
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }
}
