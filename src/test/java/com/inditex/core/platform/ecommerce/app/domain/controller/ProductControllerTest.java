package com.inditex.core.platform.ecommerce.app.domain.controller;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import com.inditex.core.platform.ecommerce.app.domain.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController priceController;

    private Product product;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(priceController).build();

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
    public void testGetPrice_found() throws Exception {
        when(productService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T10:00:00")))
                .thenReturn(product);

        mockMvc.perform(get("/products/get-price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    public void testGetPrice_notFound() throws Exception {
        when(productService.getApplicablePrice(35455, 1, LocalDateTime.parse("2020-06-14T16:00:00")))
                .thenReturn(null);

        mockMvc.perform(get("/products/get-price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.saveProduct(any(Product.class)))
                .thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("{\"brandId\":1, \"productId\":35455, \"priceList\":1, \"priority\":0, \"price\":35.50, \"currency\":\"EUR\", \"startDate\":\"2020-06-14T00:00:00\", \"endDate\":\"2020-12-31T23:59:59\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    public void testCreateProduct_invalidInput() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("{\"brandId\":1, \"priceList\":1, \"priority\":0, \"currency\":\"EUR\", \"startDate\":\"2020-06-14T00:00:00\", \"endDate\":\"2020-12-31T23:59:59\"}"))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    public void testDeleteProduct_notFound() throws Exception {
        doThrow(new IllegalArgumentException("Product not found")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct_errorDuringSave() throws Exception {
        when(productService.saveProduct(any(Product.class)))
                .thenReturn(null);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("{\"productId\":35455, \"priceList\":1, \"priority\":0, \"price\":35.50, \"currency\":\"EUR\", \"startDate\":\"2020-06-14T00:00:00\", \"endDate\":\"2020-12-31T23:59:59\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error creating product."));
    }

}
