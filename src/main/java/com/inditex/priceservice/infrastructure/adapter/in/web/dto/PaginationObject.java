package com.inditex.priceservice.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationObject {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Integer pageElements;
    private Long totalElements;
    private Boolean isLastPage;
}
