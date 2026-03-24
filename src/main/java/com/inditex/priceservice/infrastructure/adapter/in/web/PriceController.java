package com.inditex.priceservice.infrastructure.adapter.in.web;

import com.inditex.priceservice.application.usecase.GetPriceUseCase;
import com.inditex.priceservice.domain.exception.PriceNotFoundException;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.infrastructure.adapter.in.web.dto.PriceResponse;
import com.inditex.priceservice.infrastructure.adapter.in.web.mapper.PriceResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Adaptador de Entrada (REST Controller).
 * Responsabilidad única: gestionar la petición HTTP y delegar al caso de uso.
 * Depende de la interfaz GetPriceUseCase (DIP).
 */
@RestController
@RequestMapping("/api/v1/prices")
@Tag(name = "Prices", description = "Price operations API")
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceResponseMapper priceResponseMapper;

    public PriceController(GetPriceUseCase getPriceUseCase, PriceResponseMapper priceResponseMapper) {
        this.getPriceUseCase = getPriceUseCase;
        this.priceResponseMapper = priceResponseMapper;
    }

    @Operation(summary = "Get applicable price", description = "Returns the applicable price for a given product and brand at a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                         content = @Content(schema = @Schema(implementation = PriceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                         content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Price not found",
                         content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                         content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<PriceResponse> getPrice(
            @Parameter(description = "Date of application (ISO-8601)", example = "2020-06-14T10:00:00")
            @RequestParam("application-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @Parameter(description = "Product Identifier", example = "35455")
            @RequestParam("product-id") Integer productIdRaw,
            @Parameter(description = "Brand Identifier", example = "1")
            @RequestParam("brand-id") Integer brandIdRaw) {

        ProductId productId = new ProductId(productIdRaw);
        BrandId brandId = new BrandId(brandIdRaw);

        PriceResponse response = getPriceUseCase.execute(applicationDate, productId, brandId)
                .map(priceResponseMapper::toResponse)
                .orElseThrow(() -> new PriceNotFoundException("No applicable price found"));

        return ResponseEntity.ok(response);
    }
}
