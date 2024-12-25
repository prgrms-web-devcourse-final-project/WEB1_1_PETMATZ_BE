package com.petmatz.infra.pet;

import com.petmatz.domain.pet.PetClient;
import com.petmatz.domain.pet.dto.OpenApiPetInfo;
import com.petmatz.infra.open_api.client.OpenApiFeignClient;
import com.petmatz.infra.open_api.dto.OpenApiResponse;
import com.petmatz.infra.open_api.vo.PetInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetApiClient implements PetClient {

    private final OpenApiFeignClient openApiFeignClient;


    @Override
    public OpenApiPetInfo fetchPetInfo(String dogRegNo, String ownerNm) {
        OpenApiResponse<PetInfo> animalInfo = openApiFeignClient.getAnimalInfo(dogRegNo, ownerNm);
        return animalInfo.getItem().of();
    }
}
