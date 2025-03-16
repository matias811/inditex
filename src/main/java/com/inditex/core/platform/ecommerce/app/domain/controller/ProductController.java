package com.inditex.core.platform.ecommerce.app.domain.controller;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import com.inditex.core.platform.ecommerce.app.domain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/get-price")
    public ResponseEntity<Product> getPrice(
            @RequestParam("productId") Integer productId,
            @RequestParam("brandId") Integer brandId,
            @RequestParam("applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

        LOGGER.info("Product Controller - Received request with productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);

        Product product = productService.getApplicablePrice(productId, brandId, applicationDate);

        if (product != null) {
            LOGGER.info("Product Controller - Found price: {}", product.getPrice());
            return ResponseEntity.ok(product);
        } else {
            LOGGER.info("Product Controller - No price found for the given parameters.");
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        LOGGER.info("Product Controller - Received request : {}", product);

        Product savedProduct = productService.saveProduct(product);

        if (savedProduct == null) {
            LOGGER.error("Product Controller - Error creating product.");
            return ResponseEntity.badRequest().body("Error creating product.");
        } else {
            LOGGER.info("Product Controller - Product saved in database.");
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        LOGGER.info("Product Controller - Deleting product with ID : {}", id);

        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            LOGGER.error("Product Controller - Error: Product not found with ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
