package com.petmatz.domain.pet.vo;

import com.petmatz.api.pet.dto.PetUpdateRequest;
import com.petmatz.domain.pet.Gender;
import com.petmatz.domain.pet.Size;
import lombok.Builder;

@Builder
public record PetUpdateInfo(
        int age,
        String breed,
        String comment,
        Gender gender,
        String neuterYn,
        String petName,
        String profileImg,
        Size size, // 사용자가 선택한 사이즈
        String temperament
) {

    public static PetUpdateInfo of(PetUpdateRequest petUpdateRequest) {
        return PetUpdateInfo.builder()
                .age(petUpdateRequest.age())
                .breed(petUpdateRequest.breed())
                .comment(petUpdateRequest.comment())
                .gender(petUpdateRequest.gender())
                .neuterYn(petUpdateRequest.neuterYn())
                .petName(petUpdateRequest.petName())
                .profileImg(petUpdateRequest.profileImg())
                .size(petUpdateRequest.size())
                .temperament(petUpdateRequest.temperament())
                .build();
    }
}
