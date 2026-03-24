package com.inditex.priceservice.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    @Test
    @DisplayName("BrandId válido se crea correctamente")
    void brandId_valid() {
        BrandId brandId = new BrandId(1);
        assertEquals(1, brandId.getValue());
    }

    @Test
    @DisplayName("BrandId nulo lanza IllegalArgumentException")
    void brandId_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> new BrandId(null));
    }

    @Test
    @DisplayName("BrandId negativo lanza IllegalArgumentException")
    void brandId_negative_throws() {
        assertThrows(IllegalArgumentException.class, () -> new BrandId(-1));
    }

    @Test
    @DisplayName("ProductId válido se crea correctamente")
    void productId_valid() {
        ProductId productId = new ProductId(35455);
        assertEquals(35455, productId.getValue());
    }

    @Test
    @DisplayName("ProductId nulo lanza IllegalArgumentException")
    void productId_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProductId(null));
    }

    @Test
    @DisplayName("Money válido se crea correctamente")
    void money_valid() {
        Money money = new Money(new BigDecimal("35.50"), "EUR");
        assertEquals(new BigDecimal("35.50"), money.getAmount());
        assertEquals("EUR", money.getCurrency());
    }

    @Test
    @DisplayName("Money con importe negativo lanza IllegalArgumentException")
    void money_negativeAmount_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-1"), "EUR"));
    }

    @Test
    @DisplayName("Money con moneda vacía lanza IllegalArgumentException")
    void money_emptyCurrency_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, ""));
    }
}
