package com.petmatz.domain.pet;

import com.petmatz.domain.pet.dto.PetServiceDto;
import com.petmatz.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PetService {
    PetServiceDto fetchPetInfo(String dogRegNo, String ownerNm); //외부 API를 호출하여 동물 등록 정보를 가져옵니다.
    void savePet(User user, PetServiceDto dto);  //새로운 펫 정보를 저장합니다.
    void updatePet(Long petId, User user, PetServiceDto updatedDto); //기존 펫 정보를 업데이트합니다.
    void deletePet(Long petId, User user); //특정 펫 정보를 삭제합니다.
}

