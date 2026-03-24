package com.inditex.priceservice.infrastructure.adapter.in.web.mapper;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.infrastructure.adapter.in.web.dto.PriceResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper de entrada: Price (dominio) → PriceResponse (DTO).
 * Responsabilidad única: transformación entre capas (SRP).
 */
@Component
public class PriceResponseMapper {

    public PriceResponse toResponse(Price price) {
        return PriceResponse.builder()
                .productId(price.getProductId().getValue())
                .brandId(price.getBrandId().getValue())
                .priceList(price.getPriceList())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .price(price.getPrice().getAmount())
                .currency(price.getPrice().getCurrency())
                .build();
    }
}
