package com.inditex.priceservice.infrastructure.adapter.out.persistence.mapper;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PriceEntityMapperTest {

    private final PriceEntityMapper mapper = new PriceEntityMapper();

    @Test
    @DisplayName("toDomain mapea correctamente todos los campos de PriceEntity a Price")
    void toDomain_mapsAllFields() {
        PriceEntity entity = PriceEntity.builder()
                .id(1L)
                .brandId(1)
                .productId(35455)
                .priceList(2)
                .priority(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .price(new BigDecimal("25.45"))
                .curr("EUR")
                .build();

        Price price = mapper.toDomain(entity);

        assertNotNull(price);
        assertEquals(1, price.getBrandId().getValue());
        assertEquals(35455, price.getProductId().getValue());
        assertEquals(2, price.getPriceList());
        assertEquals(1, price.getPriority());
        assertEquals(new BigDecimal("25.45"), price.getPrice().getAmount());
        assertEquals("EUR", price.getPrice().getCurrency());
        assertEquals(entity.getStartDate(), price.getStartDate());
        assertEquals(entity.getEndDate(), price.getEndDate());
    }
}
