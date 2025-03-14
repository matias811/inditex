package com.inditex.core.platform.ecommerce.app.domain.service;

import com.inditex.core.platform.ecommerce.app.domain.model.Price;
import com.inditex.core.platform.ecommerce.app.domain.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceService priceService;

    private Price price;

    @BeforeEach
    public void setUp() {
        price = Price.builder()
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
        when(priceRepository.findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00")))
                .thenReturn(Collections.singletonList(price));

        Price result = priceService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00"));

        assertNotNull(result);
        assertEquals(price.getPrice(), result.getPrice());
        verify(priceRepository, times(1)).findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00"));
    }

    @Test
    public void testGetApplicablePrice_notFound() {
        when(priceRepository.findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00")))
                .thenReturn(Collections.emptyList());

        Price result = priceService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00"));

        assertNull(result);
        verify(priceRepository, times(1)).findApplicablePrices(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00"));
    }
}
