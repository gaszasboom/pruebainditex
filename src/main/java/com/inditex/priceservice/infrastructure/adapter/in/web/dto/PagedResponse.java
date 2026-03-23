package com.inditex.priceservice.infrastructure.adapter.in.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class PagedResponse<T> {
    private PaginationObject paginationObject;
    @Builder.Default
    private List<Object> sortingObject = Collections.emptyList();
    private List<T> businessObject;
}
