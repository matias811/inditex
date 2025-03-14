package com.inditex.core.platform.ecommerce.app.domain.controller;

import com.inditex.core.platform.ecommerce.app.domain.model.Price;
import com.inditex.core.platform.ecommerce.app.domain.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/get-price")
public class PriceController {

    @Autowired
    private PriceService priceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceController.class);

    @GetMapping
    public ResponseEntity<Price> getPrice(
            @RequestParam("productId") Integer productId,
            @RequestParam("brandId") Integer brandId,
            @RequestParam("applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

        LOGGER.info("Price Controller - Received request with productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);

        Price price = priceService.getApplicablePrice(productId, brandId, applicationDate);

        if (price != null) {
            LOGGER.info("Price Controller - Found price: {}", price.getPrice());
            return ResponseEntity.ok(price);
        } else {
            LOGGER.info("Price Controller - No price found for the given parameters.");
            return ResponseEntity.noContent().build();
        }
    }
}
