package com.petmatz.api.sosboard.dto;

import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.domain.sosboard.PaymentType;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;
import com.petmatz.domain.user.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public record SosBoardCreateRequestDto(
        Long userId,
        String title,
        String paymentType,
        int price,
        String comment,
        List<Long> petIds, // 여러 펫 ID를 받음
        List<PetResponse> petResponses, // PetResponse를 포함,
        String startDate, // 프론트에서 받은 날짜 문자열
        String endDate    // 프론트에서 받은 날짜 문자열
) {
    // 변환 메서드, 컨트롤러 계층에서 요청 데이터를 서비스 계층으로 전달할 때 변환함
    public SosBoardServiceDto toServiceDto(User user) {
        return new SosBoardServiceDto(
                user.getId(),
                this.title(),
                PaymentType.fromString(this.paymentType()),
                this.price(),
                this.comment(),
                this.petIds(),
                this.petResponses(),
                this.startDate(),
                this.endDate(),
                user.getNickname(),
                user.getProfileImg(),
                user.getGender().toString(),
                user.getRegion()
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
