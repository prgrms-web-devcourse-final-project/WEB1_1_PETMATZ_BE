package com.petmatz.api.sosboard.dto;

import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public record SosBoardCreateRequestDto(
        Long userId,
        String title,
        String region,
        String paymentType,
        int price,
        String comment,
        List<Long> petIds, // 여러 펫 ID를 받음
        String startDate, // 프론트에서 받은 날짜 문자열
        String endDate    // 프론트에서 받은 날짜 문자열
) {
    // 변환 메서드
    public SosBoardServiceDto toServiceDto() {
        validateDateFormat(startDate);
        validateDateFormat(endDate);

        return new SosBoardServiceDto(
                this.userId,
                this.title,
                this.region,
                SosBoard.PaymentType.fromString(this.paymentType),
                this.price,
                this.comment,
                this.petIds,
                this.startDate, // String 그대로 전달
                this.endDate    // String 그대로 전달
        );
    }


    // 날짜 형식 유효성 검증
    private void validateDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime.parse(date, formatter); // 유효성 검증
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date);
        }
    }
}
