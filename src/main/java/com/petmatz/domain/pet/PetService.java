package com.petmatz.domain.pet;

import com.petmatz.domain.pet.dto.PetSaveResponse;
import com.petmatz.domain.pet.dto.PetServiceDto;
import com.petmatz.domain.pet.dto.PetUpdateResponse;
import com.petmatz.domain.user.entity.User;

import java.net.MalformedURLException;

public interface PetService {
    PetServiceDto fetchPetInfo(String dogRegNo, String ownerNm); //외부 API를 호출하여 동물 등록 정보를 가져옵니다.
    PetSaveResponse savePet(User user, PetServiceDto dto) throws MalformedURLException;  //새로운 펫 정보를 저장합니다.
    PetUpdateResponse updatePet(Long petId, User user, PetServiceDto updatedDto) throws MalformedURLException; //기존 펫 정보를 업데이트합니다.
    void deletePet(Long petId, User user); //특정 펫 정보를 삭제합니다.
}

