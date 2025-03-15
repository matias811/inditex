package com.inditex.core.platform.ecommerce.app.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PRICES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Brand ID cannot be null")
    @Column(name = "BRAND_ID", nullable = false)
    private Integer brandId;

    @NotNull(message = "Start date cannot be null")
    @Column(name = "START_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Column(name = "END_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull(message = "Price list cannot be null")
    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;

    @NotNull(message = "Product ID cannot be null")
    @Column(name = "PRODUCT_ID", nullable = false)
    private Integer productId;

    @NotNull(message = "Priority cannot be null")
    @Positive(message = "Priority must be a positive number")
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Currency cannot be null")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @Column(name = "CURR", nullable = false, length = 3)
    private String currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getBrandId(), product.getBrandId()) && Objects.equals(getStartDate(), product.getStartDate()) && Objects.equals(getEndDate(), product.getEndDate()) && Objects.equals(getPriceList(), product.getPriceList()) && Objects.equals(getProductId(), product.getProductId()) && Objects.equals(getPriority(), product.getPriority()) && Objects.equals(getPrice(), product.getPrice()) && Objects.equals(getCurrency(), product.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrandId(), getStartDate(), getEndDate(), getPriceList(), getProductId(), getPriority(), getPrice(), getCurrency());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", priceList=" + priceList +
                ", productId=" + productId +
                ", priority=" + priority +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
