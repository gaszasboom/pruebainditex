package com.inditex.priceservice.infrastructure.adapter.out.persistence.repository;

import com.inditex.priceservice.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Query nativa SQL para máxima eficiencia.
     * Delega la selección por prioridad a la BD con LIMIT 1, evitando carga de múltiples registros.
     */
    @Query(value = "SELECT * FROM PRICES " +
            "WHERE PRODUCT_ID = :productId " +
            "AND BRAND_ID = :brandId " +
            "AND :applicationDate BETWEEN START_DATE AND END_DATE " +
            "ORDER BY PRIORITY DESC LIMIT 1",
            nativeQuery = true)
    Optional<PriceEntity> findApplicablePrice(
            @Param("applicationDate") LocalDateTime applicationDate,
            @Param("productId") Integer productId,
            @Param("brandId") Integer brandId
    );
}
