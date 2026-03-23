package com.inditex.priceservice.application.usecase;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.domain.port.PriceRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio de Aplicación (Use Case).
 * Implementa la lógica de negocio para obtener la tarifa aplicable.
 * Delega la recuperación de datos al puerto (PriceRepositoryPort)
 * invirtiendo la dependencia (I de SOLID: Dependency Inversion).
 */
@Service
public class GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    public GetPriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    /**
     * Ejecuta el caso de uso para consultar el precio.
     * 
     * @param applicationDate Fecha en la que se quiere consultar el precio.
     * @param productId       Identificador del producto (Value Object).
     * @param brandId         Identificador de la cadena (Value Object).
     * @return Price opcional con los datos de tarifa.
     */
    public Optional<Price> execute(LocalDateTime applicationDate, ProductId productId, BrandId brandId) {
        return priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId);
    }
}
