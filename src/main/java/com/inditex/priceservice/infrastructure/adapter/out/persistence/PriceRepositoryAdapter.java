package com.inditex.priceservice.infrastructure.adapter.out.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.Money;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.domain.port.PriceRepositoryPort;
import com.inditex.priceservice.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.inditex.priceservice.infrastructure.adapter.out.persistence.repository.SpringDataPriceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adaptador de Salida de Persistencia.
 * Implementa el puerto del dominio (PriceRepositoryPort),
 * transformando entidades JPA (PriceEntity) hacia modelos de Dominio (Price y Value Objects).
 */
@Component
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final SpringDataPriceRepository springDataPriceRepository;

    public PriceRepositoryAdapter(SpringDataPriceRepository springDataPriceRepository) {
        this.springDataPriceRepository = springDataPriceRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, ProductId productId, BrandId brandId) {
        return springDataPriceRepository.findApplicablePrice(applicationDate, productId.getValue(), brandId.getValue())
                .map(this::toDomain);
    }

    private Price toDomain(PriceEntity entity) {
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
