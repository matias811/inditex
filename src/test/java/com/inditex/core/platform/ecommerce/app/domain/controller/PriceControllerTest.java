package com.inditex.core.platform.ecommerce.app.domain.controller;

import com.inditex.core.platform.ecommerce.app.domain.model.Price;
import com.inditex.core.platform.ecommerce.app.domain.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PriceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private PriceController priceController;

    private Price price;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(priceController).build();

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
    public void testGetPrice_found() throws Exception {
        when(priceService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00")))
                .thenReturn(price);

        mockMvc.perform(get("/get-price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    public void testGetPrice_notFound() throws Exception {
        when(priceService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00")))
                .thenReturn(null);

        mockMvc.perform(get("/get-price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isNoContent());
    }
}
