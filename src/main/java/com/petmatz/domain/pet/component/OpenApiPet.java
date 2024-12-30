package com.petmatz.domain.pet.component;

import com.petmatz.domain.pet.PetClient;
import com.petmatz.domain.pet.dto.OpenApiPetInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenApiPet {

    private final PetClient petClient;

    public OpenApiPetInfo getOpenApiPetInfo(String dogRegNo, String ownerNm) {
        return petClient.fetchPetInfo(dogRegNo, ownerNm);
    }
}
