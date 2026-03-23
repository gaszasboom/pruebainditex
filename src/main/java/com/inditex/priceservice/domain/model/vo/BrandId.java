package com.inditex.priceservice.domain.model.vo;

import lombok.Value;

/**
 * Value Object Patrón DDD.
 * Representa la identidad de una Cadena (Brand).
 */
@Value
public class BrandId {
    Integer value;

    public BrandId(Integer value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Brand ID inválido");
        }
        this.value = value;
    }
}
