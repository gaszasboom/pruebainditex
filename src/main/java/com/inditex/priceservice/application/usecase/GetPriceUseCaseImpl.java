package com.inditex.priceservice.application.usecase;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.domain.port.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementación del caso de uso.
 * Delega la recuperación de datos al puerto de salida (Dependency Inversion).
 */
public class GetPriceUseCaseImpl implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    public GetPriceUseCaseImpl(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Optional<Price> execute(LocalDateTime applicationDate, ProductId productId, BrandId brandId) {
        return priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId);
    }
}
