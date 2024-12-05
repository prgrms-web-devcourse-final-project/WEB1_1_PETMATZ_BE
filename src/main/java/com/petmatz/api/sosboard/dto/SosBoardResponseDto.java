package com.petmatz.api.sosboard.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.domain.sosboard.PaymentType;
import com.petmatz.domain.sosboard.PetSosBoard;
import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SosBoardResponseDto(
        Long id,
        Long userId,
        String accountId,
        String title,
        PaymentType paymentType,
        int price,
        String startDate, // "yyyy-MM-dd HH:mm" 형식
        String endDate,    // "yyyy-MM-dd HH:mm" 형식
        List<PetResponse> pets,
        String authorNickname,
        String authorProfileImg,
        String authorGender,
        String authorRegion,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime createdAt, // 생성일시
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime updatedAt
) {
    public static SosBoardResponseDto of(SosBoard sosBoard) {
        return new SosBoardResponseDto(
                sosBoard.getId(),
                sosBoard.getUser().getId(),
                sosBoard.getUser().getAccountId(),
                sosBoard.getTitle(),
                sosBoard.getPaymentType(),
                sosBoard.getPrice(),
                sosBoard.getStartDate(), // String 그대로 반환
                sosBoard.getEndDate(),
                sosBoard.getPetSosBoards().stream()
                        .map(PetSosBoard::getPet) // PetSosBoard에서 Pet 가져오기
                        .map(PetResponse::of)    // Pet → PetResponse 변환
                        .collect(Collectors.toList()),// String 그대로 반환
                sosBoard.getUser().getNickname(),
                sosBoard.getUser().getProfileImg(),
                sosBoard.getUser().getGender().toString(),
                sosBoard.getUser().getRegion(),
                sosBoard.getCreatedAt(),
                sosBoard.getUpdatedAt()
        );
    }

    public static SosBoardResponseDto fromServiceDto(SosBoardServiceDto serviceDto) {
        // PetResponse 리스트를 Service DTO에서 가져옴
        List<PetResponse> petResponses = serviceDto.petResponses();

        return new SosBoardResponseDto(
                serviceDto.id(),
                serviceDto.userId(),
                serviceDto.accountId(),
                serviceDto.title(),
                serviceDto.paymentType(),
                serviceDto.price(),
                serviceDto.startDate(),
                serviceDto.endDate(),
                petResponses, // PetResponse 리스트 직접 전달
                serviceDto.userNickname(),
                serviceDto.userProfileImg(),
                serviceDto.userGender(),
                serviceDto.userRegion(),
                serviceDto.createdAt(),
                serviceDto.updatedAt()
        );
    }
}