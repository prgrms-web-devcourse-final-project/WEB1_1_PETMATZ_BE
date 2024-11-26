package com.petmatz.api.sosboard.dto;

import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.sosboard.PetSosBoard;

import java.util.List;
import java.util.stream.Collectors;

public record SosBoardResponseDto(
        Long id,
        String title,
        String region,
        SosBoard.PaymentType paymentType,
        int price,
        String startDate, // "yyyy-MM-dd HH:mm" 형식
        String endDate,    // "yyyy-MM-dd HH:mm" 형식
        List<PetResponse> pets
) {
    public static SosBoardResponseDto of(SosBoard sosBoard) {
        return new SosBoardResponseDto(
                sosBoard.getId(),
                sosBoard.getTitle(),
                sosBoard.getRegion(),
                sosBoard.getPaymentType(),
                sosBoard.getPrice(),
                sosBoard.getStartDate(), // String 그대로 반환
                sosBoard.getEndDate(),
                sosBoard.getPetSosBoards().stream()
                        .map(PetSosBoard::getPet) // PetSosBoard에서 Pet 가져오기
                        .map(PetResponse::of)    // Pet → PetResponse 변환
                        .collect(Collectors.toList())// String 그대로 반환
        );
    }
}

