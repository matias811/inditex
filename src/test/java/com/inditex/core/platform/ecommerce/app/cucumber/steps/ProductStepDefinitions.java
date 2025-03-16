package com.inditex.core.platform.ecommerce.app.cucumber.steps;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import com.inditex.core.platform.ecommerce.app.domain.repository.ProductRepository;
import com.inditex.core.platform.ecommerce.app.domain.service.ProductService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductStepDefinitions {
    @Mock
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private Product result;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("the product with ID {string} and brand ID {string} exists")
    public void the_product_with_id_and_brand_id_exists(String productId, String brandId) {
        productService.getApplicablePrice(
                Integer.parseInt(productId),
                Integer.parseInt(brandId),
                LocalDateTime.parse("2020-06-14T10:00:00"));
    }

    @When("I request the price for product ID {string} and brand ID {string} at {string}")
    public void i_request_the_price_for_product_id_and_brand_id_at(String productId, String brandId, String dateTime) {
        result = productService.getApplicablePrice(
                Integer.parseInt(productId),
                Integer.parseInt(brandId),
                LocalDateTime.parse(dateTime)
        );
    }

    @Then("the response price should be {string} EUR")
    public void the_response_price_should_be_eur(String expectedPrice) {
        assertNotNull(result);
        assertEquals(new BigDecimal(expectedPrice), result.getPrice());
    }
}
