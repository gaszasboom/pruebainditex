package com.inditex.priceservice.domain.port;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Puerto de Salida. (Patrón Ports & Adapters / Arquitectura Hexagonal)
 * Define el contrato para recuperar datos de precios independientemente
 * de la infraestructura (BD, API paralela, etc.).
 */
public interface PriceRepositoryPort {
    Optional<Price> findApplicablePrice(LocalDateTime applicationDate, ProductId productId, BrandId brandId);
}
