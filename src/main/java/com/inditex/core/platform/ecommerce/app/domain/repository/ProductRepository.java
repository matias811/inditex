package com.inditex.core.platform.ecommerce.app.domain.repository;

import com.inditex.core.platform.ecommerce.app.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    SELECT p FROM Product p 
    WHERE p.productId = :productId 
    AND p.brandId = :brandId 
    AND :date BETWEEN p.startDate AND p.endDate
    ORDER BY p.priority DESC
    """)
    List<Product> findApplicablePrices(@Param("productId") Integer productId,
                                       @Param("brandId") Integer brandId,
                                       @Param("date") LocalDateTime date);

}
