package com.petmatz.api.sosboard.dto;

import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.domain.sosboard.PaymentType;
import com.petmatz.domain.sosboard.PetSosBoard;
import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public record SosBoardCreateResponseDto(
        Long id,
        String title,
        PaymentType paymentType,
        int price,
        String startDate,
        String endDate,
        List<PetResponse> pets,
        String authorNickname,
        String authorProfileImg,
        String authorGender,
        String authorRegion
) {
    public static SosBoardCreateResponseDto of(SosBoard sosBoard) {
        User user = sosBoard.getUser();
        return new SosBoardCreateResponseDto(
                sosBoard.getId(),
                sosBoard.getTitle(),
                sosBoard.getPaymentType(),
                sosBoard.getPrice(),
                sosBoard.getStartDate(),
                sosBoard.getEndDate(),
                sosBoard.getPetSosBoards().stream()
                        .map(PetSosBoard::getPet)
                        .map(PetResponse::of)
                        .collect(Collectors.toList()),
                user.getNickname(),
                user.getProfileImg(),
                user.getGender().toString(),
                user.getRegion()
        );
    }
}
