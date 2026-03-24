package com.inditex.priceservice.infrastructure.adapter.out.persistence.mapper;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.Money;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper de infraestructura: PriceEntity → Price (dominio).
 * Responsabilidad única: transformación entre capas (SRP).
 */
@Component
public class PriceEntityMapper {

    public Price toDomain(PriceEntity entity) {
        return Price.builder()
                .brandId(new BrandId(entity.getBrandId()))
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(new ProductId(entity.getProductId()))
                .priority(entity.getPriority())
                .price(new Money(entity.getPrice(), entity.getCurr()))
                .build();
    }
}
