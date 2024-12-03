package com.petmatz.domain.sosboard.dto;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        long totalCount,
        int totalPages,
        int currentPage
) {
    public static <T> PageResponseDto<T> of(List<T> content, long totalCount, int totalPages, int currentPage) {
        return new PageResponseDto<>(content, totalCount, totalPages, currentPage);
    }
}

