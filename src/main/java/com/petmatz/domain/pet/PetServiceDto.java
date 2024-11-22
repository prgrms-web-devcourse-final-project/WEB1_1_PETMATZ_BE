package com.petmatz.domain.pet;

import com.petmatz.api.pet.PetApiRequest;
import com.petmatz.api.pet.PetInfoDto;
import com.petmatz.api.pet.PetRequest;

public record PetServiceDto(
        String dogRegNo,
        String ownerNm,
        String petName,
        String breed,
        String gender,
        String isNeutered,
        String size,
        Integer age,
        String temperament,
        String preferredWalkingLocation,
        String profileImg,
        String comment
) {

    public static PetServiceDto of(PetInfoDto infoDto) {
        return new PetServiceDto(
                infoDto.dogRegNo(),
                null,
                infoDto.dogNm(),    // petName
                infoDto.kindNm(),   // breed
                infoDto.sexNm(),    // gender
                infoDto.neuterYn(), // isNeutered
                null,               // size (기본값)
                null,               // age (기본값)
                null,               // temperament (기본값)
                null,               // preferredWalkingLocation (기본값)
                null,               // profileImg (기본값)
                null                // comment (기본값)
        );
    }
    // 정적 팩토리 메서드: PetApiRequest → PetServiceDto
    public static PetServiceDto of(PetApiRequest request) {
        return new PetServiceDto(
                request.dogRegNo(),
                request.ownerNm(), // ownerName
                null, // petName
                null, // breed
                null, // gender
                null, // isNeutered
                null, // size
                null, // age
                null, // temperament
                null, // preferredWalkingLocation
                null, // profileImg
                null // comment
        );
    }

    // 정적 팩토리 메서드: PetRequest → PetServiceDto
    public static PetServiceDto of(PetRequest request) {
        return new PetServiceDto(
                request.dogRegNo(),
                request.ownerNm(),
                request.petName(),
                request.breed(),
                request.gender(),
                request.isNeutered(),
                request.size(),
                request.age(),
                request.temperament(),
                request.preferredWalkingLocation(),
                request.profileImg(),
                request.comment()
        );
    }

    // with 메서드: 필드 값 변경 시 활용
    public PetServiceDto withPetName(String petName) {
        return new PetServiceDto(
                this.dogRegNo,
                this.ownerNm,
                petName,
                this.breed,
                this.gender,
                this.isNeutered,
                this.size,
                this.age,
                this.temperament,
                this.preferredWalkingLocation,
                this.profileImg,
                this.comment
        );
    }
}

