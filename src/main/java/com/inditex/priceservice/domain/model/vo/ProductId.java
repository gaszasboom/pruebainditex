package com.inditex.priceservice.domain.model.vo;

import lombok.Value;

/**
 * Value Object Patrón DDD.
 * Representa la identidad de un Producto.
 */
@Value
public class ProductId {
    Integer value;

    public ProductId(Integer value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Product ID inválido");
        }
        this.value = value;
    }
}
