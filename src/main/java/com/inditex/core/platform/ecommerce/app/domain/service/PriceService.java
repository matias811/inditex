package com.inditex.core.platform.ecommerce.app.domain.service;

import com.inditex.core.platform.ecommerce.app.domain.model.Price;
import com.inditex.core.platform.ecommerce.app.domain.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceService.class);

    /**
     * Este método obtiene el precio para un producto y marca específicos en la fecha proporcionada.
     *
     * @param productId   ID del producto
     * @param brandId     ID de la marca
     * @param applicationDate Fecha de la aplicación del precio
     * @return El precio aplicable más prioritario
     */
    public Price getApplicablePrice(Integer productId, Integer brandId, LocalDateTime applicationDate) {
        LOGGER.info("Price Service - Starting call to find applicable prices.");
        List<Price> applicablePrices = priceRepository.findApplicablePrices(productId, brandId, applicationDate);

        if (applicablePrices != null && !applicablePrices.isEmpty()) {
            Price price = applicablePrices.get(0);
            LOGGER.info("Price Service - Price recovered : {}", price.getPrice());
            return price;
        }

        LOGGER.info("Price Service - No price found.");
        return null;
    }
}
