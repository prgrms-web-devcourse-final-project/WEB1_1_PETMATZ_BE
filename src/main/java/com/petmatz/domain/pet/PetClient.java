package com.petmatz.domain.pet;

import com.petmatz.domain.pet.dto.OpenApiPetInfo;

public interface PetClient {

    OpenApiPetInfo fetchPetInfo(String dogRegNo, String ownerNm);

}
