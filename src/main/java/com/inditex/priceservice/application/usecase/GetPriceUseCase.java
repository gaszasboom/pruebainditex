package com.inditex.priceservice.application.usecase;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Puerto de entrada (Use Case).
 * Define el contrato para obtener la tarifa aplicable.
 * El controlador depende de esta abstracción, no de la implementación concreta (DIP).
 */
public interface GetPriceUseCase {
    Optional<Price> execute(LocalDateTime applicationDate, ProductId productId, BrandId brandId);
}
