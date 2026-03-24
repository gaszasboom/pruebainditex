package com.inditex.priceservice.application.usecase;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.Money;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.domain.port.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el Caso de Uso (GetPriceUseCaseImpl).
 * Validan los 5 escenarios del enunciado verificando campos individuales del resultado.
 */
@ExtendWith(MockitoExtension.class)
class GetPriceUseCaseTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    private GetPriceUseCaseImpl getPriceUseCase;

    private final ProductId productId = new ProductId(35455);
    private final BrandId brandId = new BrandId(1);

    @BeforeEach
    void setUp() {
        getPriceUseCase = new GetPriceUseCaseImpl(priceRepositoryPort);
    }

    @Test
    @DisplayName("Test 1: petición a las 10:00 del día 14 de Junio de 2020 → tarifa 1, precio 35.50")
    void test1_requestAt1000_day14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Price expectedPrice = createPrice(1, 0, "35.50", "2020-06-14T00:00:00", "2020-12-31T23:59:59");

        when(priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Optional.of(expectedPrice));

        Optional<Price> result = getPriceUseCase.execute(applicationDate, productId, brandId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getPriceList());
        assertEquals(new BigDecimal("35.50"), result.get().getPrice().getAmount());
        assertEquals("EUR", result.get().getPrice().getCurrency());
        assertEquals(35455, result.get().getProductId().getValue());
        assertEquals(1, result.get().getBrandId().getValue());
    }

    @Test
    @DisplayName("Test 2: petición a las 16:00 del día 14 de Junio de 2020 → tarifa 2, precio 25.45")
    void test2_requestAt1600_day14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Price expectedPrice = createPrice(2, 1, "25.45", "2020-06-14T15:00:00", "2020-06-14T18:30:00");

        when(priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Optional.of(expectedPrice));

        Optional<Price> result = getPriceUseCase.execute(applicationDate, productId, brandId);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getPriceList());
        assertEquals(new BigDecimal("25.45"), result.get().getPrice().getAmount());
    }

    @Test
    @DisplayName("Test 3: petición a las 21:00 del día 14 de Junio de 2020 → tarifa 1, precio 35.50")
    void test3_requestAt2100_day14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0);
        Price expectedPrice = createPrice(1, 0, "35.50", "2020-06-14T00:00:00", "2020-12-31T23:59:59");

        when(priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Optional.of(expectedPrice));

        Optional<Price> result = getPriceUseCase.execute(applicationDate, productId, brandId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getPriceList());
        assertEquals(new BigDecimal("35.50"), result.get().getPrice().getAmount());
    }

    @Test
    @DisplayName("Test 4: petición a las 10:00 del día 15 de Junio de 2020 → tarifa 3, precio 30.50")
    void test4_requestAt1000_day15() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);
        Price expectedPrice = createPrice(3, 1, "30.50", "2020-06-15T00:00:00", "2020-06-15T11:00:00");

        when(priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Optional.of(expectedPrice));

        Optional<Price> result = getPriceUseCase.execute(applicationDate, productId, brandId);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getPriceList());
        assertEquals(new BigDecimal("30.50"), result.get().getPrice().getAmount());
    }

    @Test
    @DisplayName("Test 5: petición a las 21:00 del día 16 de Junio de 2020 → tarifa 4, precio 38.95")
    void test5_requestAt2100_day16() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0);
        Price expectedPrice = createPrice(4, 1, "38.95", "2020-06-15T16:00:00", "2020-12-31T23:59:59");

        when(priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Optional.of(expectedPrice));

        Optional<Price> result = getPriceUseCase.execute(applicationDate, productId, brandId);

        assertTrue(result.isPresent());
        assertEquals(4, result.get().getPriceList());
        assertEquals(new BigDecimal("38.95"), result.get().getPrice().getAmount());
    }

    @Test
    @DisplayName("Test 6: sin precio aplicable → Optional vacío")
    void test6_noApplicablePrice_returnsEmpty() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        when(priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Optional.empty());

        Optional<Price> result = getPriceUseCase.execute(applicationDate, productId, brandId);

        assertTrue(result.isEmpty());
    }

    private Price createPrice(Integer priceList, Integer priority, String priceValue, String startDateStr, String endDateStr) {
        return Price.builder()
                .brandId(brandId)
                .startDate(LocalDateTime.parse(startDateStr))
                .endDate(LocalDateTime.parse(endDateStr))
                .priceList(priceList)
                .productId(productId)
                .priority(priority)
                .price(new Money(new BigDecimal(priceValue), "EUR"))
                .build();
    }
}
