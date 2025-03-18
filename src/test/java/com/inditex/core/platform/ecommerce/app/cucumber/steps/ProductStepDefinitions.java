package com.inditex.core.platform.ecommerce.app.cucumber.steps;

import com.inditex.core.platform.ecommerce.app.cucumber.utils.ScenarioContext;
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
import org.springframework.test.context.TestContextManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class ProductStepDefinitions {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final ScenarioContext scenarioContext;

    @Mock
    private ProductRepository productRepository;

    private Product result;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public ProductStepDefinitions(@Autowired ProductService productService, @Autowired ScenarioContext scenarioContext) {
        this.productRepository = mock(ProductRepository.class);
        this.productService = new ProductService(productRepository);
        this.scenarioContext = scenarioContext;

        try {
            new TestContextManager(getClass()).prepareTestInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Given("I create a new product with:")
    public void i_create_a_new_product_with(io.cucumber.datatable.DataTable dataTable) {
        var data = dataTable.asMaps().get(0);

        Product createdProduct = Product.builder()
                .brandId(Integer.parseInt(data.get("brandId")))
                .productId(Integer.parseInt(data.get("productId")))
                .priceList(Integer.parseInt(data.get("priceList")))
                .priority(Integer.parseInt(data.get("priority")))
                .price(new BigDecimal(data.get("price")))
                .currency(data.get("currency"))
                .startDate(LocalDateTime.parse(data.get("startDate")))
                .endDate(LocalDateTime.parse(data.get("endDate")))
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(createdProduct);
        Product savedProduct = productService.saveProduct(createdProduct);

        assertNotNull(savedProduct);
        scenarioContext.set("createdProduct", savedProduct);
    }

    @Then("the product should be successfully created")
    public void the_product_should_be_successfully_created() {
        Product createdProduct = scenarioContext.get("createdProduct", Product.class);
        assertNotNull(createdProduct);
    }

    @Given("the product with product ID {string} and brand ID {string} exists")
    public void the_product_with_id_and_brand_id_exists(String productId, String brandId) {
        Product createdProduct = scenarioContext.get("createdProduct", Product.class);
        when(productRepository.findById(Long.parseLong(productId))).thenReturn(Optional.ofNullable(createdProduct));
        assertTrue(productRepository.findById(Long.parseLong(productId)).isPresent());
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
