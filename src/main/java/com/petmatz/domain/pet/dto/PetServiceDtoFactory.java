package com.petmatz.domain.pet.dto;

import com.petmatz.api.pet.dto.PetApiRequest;
import com.petmatz.api.pet.dto.PetInfoDto;
import com.petmatz.api.pet.dto.PetRequest;

public class PetServiceDtoFactory {

    // PetInfoDto → PetServiceDto 변환
    public static PetServiceDto from(PetInfoDto infoDto) {
        return new PetServiceDto(
                null, // id (기본값)
                infoDto.dogRegNo(),
                null, // ownerNm
                infoDto.dogNm(),
                infoDto.kindNm(),
                infoDto.sexNm(),
                infoDto.neuterYn(),
                null, // size (기본값)
                0,    // age (기본값)
                null, // temperament (기본값)
                null, // preferredWalkingLocation (기본값)
                null, // profileImg (기본값)
                null  // comment (기본값)
        );
    }

    // PetApiRequest → PetServiceDto 변환
    public static PetServiceDto from(PetApiRequest request) {
        return new PetServiceDto(
                null, // id (기본값)
                request.dogRegNo(),
                request.ownerNm(),
                null, // petName
                null, // breed
                null, // gender
                null, // neuterYn
                null, // size
                0,    // age (기본값)
                null, // temperament (기본값)
                null, // preferredWalkingLocation
                null, // profileImg
                null  // comment
        );
    }

    // PetRequest → PetServiceDto 변환
    public static PetServiceDto from(PetRequest request) {
        return new PetServiceDto(
                null, // id (기본값)
                request.dogRegNo(),
                request.ownerNm(),
                request.petName(),
                request.breed(),
                request.gender(),
                request.neuterYn(),
                request.size(),
                request.age() != null ? request.age() : 0, // null 체크 후 기본값 설정
                request.temperament(),
                request.preferredWalkingLocation(),
                request.profileImg(),
                request.comment()
        );
    }
}

