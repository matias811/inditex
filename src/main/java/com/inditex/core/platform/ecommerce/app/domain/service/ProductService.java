package com.inditex.core.platform.ecommerce.app.domain.service;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import com.inditex.core.platform.ecommerce.app.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    /**
     * This method obtains the price for a product and specific brand given a date
     *
     * @param productId   ID of product
     * @param brandId     ID of product brand
     * @param applicationDate Date
     * @return Applicable Price
     */
    public Product getApplicablePrice(Integer productId, Integer brandId, LocalDateTime applicationDate) {
        LOGGER.info("Price Service - Starting call to find applicable prices.");
        List<Product> applicableProducts = productRepository.findApplicablePrices(productId, brandId, applicationDate);

        if (applicableProducts != null && !applicableProducts.isEmpty()) {
            Product product = applicableProducts.get(0);
            LOGGER.info("Price Service - Price recovered : {}", product.getPrice());
            return product;
        }

        LOGGER.info("Price Service - No price found.");
        return null;
    }

    /**
     * This method creates a product and saves it in the h2 database
     *
     * @param product
     * @return Product
     */
    public Product saveProduct(Product product) {

        LOGGER.info("Product Service - Creating new product with request : {}", product);

        Product newProduct = productRepository.save(product);

        LOGGER.info("Product Service - Product created : {}", newProduct);

        return newProduct;
    }

    /**
     * This method deletes a product from the h2 databse
     *
     * @param id
     */
    public void deleteProduct(Long id) {

        LOGGER.info("Product Service - Deleting product with id : {}", id);

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            LOGGER.info("Product Service - Product found with id : {}", id);
            productRepository.deleteById(id);
            LOGGER.info("Product Service - Product with id {} deleted from database.", id);
        } else {
            LOGGER.info("Product Service - No product was found with id : {}", id);
        }
    }
}
