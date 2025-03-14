package com.inditex.core.platform.ecommerce.app.domain.repository;

import com.inditex.core.platform.ecommerce.app.domain.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("""
    SELECT p FROM Price p 
    WHERE p.productId = :productId 
    AND p.brandId = :brandId 
    AND :date BETWEEN p.startDate AND p.endDate
    ORDER BY p.priority DESC
    """)
    List<Price> findApplicablePrices(@Param("productId") Integer productId,
                                     @Param("brandId") Integer brandId,
                                     @Param("date") LocalDateTime date);

}
