package com.inditex.priceservice.infrastructure.adapter.in.web;

import com.inditex.priceservice.application.usecase.GetPriceUseCase;
import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.model.vo.BrandId;
import com.inditex.priceservice.domain.model.vo.ProductId;
import com.inditex.priceservice.infrastructure.adapter.in.web.dto.PagedResponse;
import com.inditex.priceservice.infrastructure.adapter.in.web.dto.PaginationObject;
import com.inditex.priceservice.infrastructure.adapter.in.web.dto.PriceResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;

/**
 * Adaptador de Entrada (REST Controller).
 */
@RestController
@RequestMapping("/api/prices/v1")
@Tag(name = "Prices", description = "Price operations API")
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;

    public PriceController(GetPriceUseCase getPriceUseCase) {
        this.getPriceUseCase = getPriceUseCase;
    }

    @Operation(summary = "Get applicable price", description = "Returns the applicable price for a given product and brand at a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", 
                         content = @Content(schema = @Schema(implementation = PagedResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", 
                         content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Price not found", 
                         content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                         content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<PagedResponse<PriceResponse>> getPrice(
            @Parameter(description = "Date of application (ISO-8601)", example = "2020-06-14T10:00:00") @RequestParam("application_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @Parameter(description = "Product Identifier", example = "35455") @RequestParam("product_id") Integer productIdRaw,
            @Parameter(description = "Brand Identifier", example = "1") @RequestParam("brand_id") Integer brandIdRaw) {
        
        ProductId productId = new ProductId(productIdRaw);
        BrandId brandId = new BrandId(brandIdRaw);

        Price price = getPriceUseCase.execute(applicationDate, productId, brandId)
                .orElseThrow(() -> new IllegalArgumentException("No aplicable price found"));

        PriceResponse responseDto = PriceResponse.builder()
                .productId(price.getProductId().getValue())
                .brandId(price.getBrandId().getValue())
                .priceList(price.getPriceList())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .price(price.getPrice().getAmount())
                .curr(price.getPrice().getCurrency())
                .build();

        PaginationObject pagination = PaginationObject.builder()
                .currentPage(1)
                .pageSize(1)
                .totalPages(1)
                .pageElements(1)
                .totalElements(1L)
                .isLastPage(true)
                .build();

        PagedResponse<PriceResponse> body = PagedResponse.<PriceResponse>builder()
                .paginationObject(pagination)
                .businessObject(List.of(responseDto))
                .build();

        return ResponseEntity.ok(body);
    }
}
