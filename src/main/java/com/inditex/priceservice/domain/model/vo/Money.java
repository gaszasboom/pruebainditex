package com.inditex.priceservice.domain.model.vo;

import lombok.Value;

import java.math.BigDecimal;

/**
 * Value Object Patrón DDD.
 * Representa un valor monetario y su moneda (curr).
 * Garantiza inmutabilidad y reglas de validación básicas de negocio.
 */
@Value
public class Money {
    BigDecimal amount;
    String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo o nulo");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía");
        }
        this.amount = amount;
        this.currency = currency;
    }
}
