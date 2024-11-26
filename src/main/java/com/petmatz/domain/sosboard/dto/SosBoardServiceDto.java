package com.petmatz.domain.sosboard.dto;

import com.petmatz.api.sosboard.dto.SosBoardCreateRequestDto;
import com.petmatz.domain.sosboard.SosBoard;

import java.util.List;

public record SosBoardServiceDto(
        Long userId,
        String title,
        String region,
        SosBoard.PaymentType paymentType,
        Integer price,
        String comment,
        List<Long> petIds,
        String startDate, // 변환된 LocalDateTime
        String endDate    // 변환된 LocalDateTime
) {
    // 정적 팩토리 메서드: SosBoardCreateRequestDto → SosBoardServiceDto
    public static SosBoardServiceDto from(SosBoardCreateRequestDto requestDto) {
        return new SosBoardServiceDto(
                requestDto.userId(),
                requestDto.title(),
                requestDto.region(),
                SosBoard.PaymentType.fromString(requestDto.paymentType()), // String → PaymentType 변환
                requestDto.price(),
                requestDto.comment(),
                requestDto.petIds(),
                requestDto.startDate(),
                requestDto.endDate()
        );
    }
}

