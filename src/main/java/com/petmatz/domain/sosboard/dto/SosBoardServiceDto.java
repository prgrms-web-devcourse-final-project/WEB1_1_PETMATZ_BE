package com.petmatz.domain.sosboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.domain.sosboard.PaymentType;
import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SosBoardServiceDto(
        Long id, //게시글 id
        Long userId, //유저 id
        String accountId,
        String title,
        PaymentType paymentType,
        Integer price,
        String comment,
        List<Long> petIds,
        List<PetResponse> petResponses, // PetResponse를 포함
        String startDate, // 변환된 LocalDateTime
        String endDate,    // 변환된 LocalDateTime
        String userNickname,
        String userProfileImg,
        String userGender,
        String userRegion,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime createdAt, // 생성일시
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime updatedAt

) {

    // SosBoard → SosBoardServiceDto 변환
    public static SosBoardServiceDto from(SosBoard sosBoard,  List<PetResponse> petResponses) {
        return new SosBoardServiceDto(
                sosBoard.getId(),
                sosBoard.getUser().getId(),
                sosBoard.getUser().getAccountId(),
                sosBoard.getTitle(),
                sosBoard.getPaymentType(),
                sosBoard.getPrice(),
                sosBoard.getComment(),
                sosBoard.getPetSosBoards().stream()
                        .map(petSosBoard -> petSosBoard.getPet().getId())
                        .collect(Collectors.toList()),
                petResponses,
                sosBoard.getStartDate(),
                sosBoard.getEndDate(),
                sosBoard.getUser().getNickname(),
                sosBoard.getUser().getProfileImg(),
                sosBoard.getUser().getGender().toString(),
                sosBoard.getUser().getRegion(),
                sosBoard.getCreatedAt(),
                sosBoard.getUpdatedAt()
                        );
    }

    public SosBoard toEntity(User user) {
        return SosBoard.builder()
                .user(user)
                .title(this.title())
                .paymentType(this.paymentType())
                .price(this.price())
                .comment(this.comment())
                .startDate(this.startDate())
                .endDate(this.endDate())
                .build();
    }
}

