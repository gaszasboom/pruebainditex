package com.inditex.priceservice.infrastructure.adapter.out.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.domain.port.PriceRepositoryPort;
import com.inditex.priceservice.infrastructure.adapter.out.persistence.mapper.PriceEntityMapper;
import com.inditex.priceservice.infrastructure.adapter.out.persistence.repository.SpringDataPriceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adaptador de Salida de Persistencia.
 * Implementa el puerto del dominio (PriceRepositoryPort).
 * Delega el mapeo a PriceEntityMapper (SRP).
 */
@Component
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final SpringDataPriceRepository springDataPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    public PriceRepositoryAdapter(SpringDataPriceRepository springDataPriceRepository,
                                   PriceEntityMapper priceEntityMapper) {
        this.springDataPriceRepository = springDataPriceRepository;
        this.priceEntityMapper = priceEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, ProductId productId, BrandId brandId) {
        return springDataPriceRepository
                .findApplicablePrice(applicationDate, productId.getValue(), brandId.getValue())
                .map(priceEntityMapper::toDomain);
    }
}
