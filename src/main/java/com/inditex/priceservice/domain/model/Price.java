package com.inditex.priceservice.domain.model;

import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.Money;
import com.inditex.priceservice.domain.model.vo.ProductId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Aggregate Root Patrón DDD.
 * Entidad principal del modelo de dominio de Precios.
 * Agrupa los distintos Value Objects.
 */
@Data
@Builder
public class Price {
    private BrandId brandId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer priceList;
    private ProductId productId;
    private Integer priority;
    private Money price;
}
