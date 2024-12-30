package com.petmatz.domain.pet;

import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.aws.vo.S3Imge;
import com.petmatz.domain.global.S3ImgDataInfo;
import com.petmatz.domain.pet.component.OpenApiPet;
import com.petmatz.domain.pet.component.PetAppend;
import com.petmatz.domain.pet.component.PetDelete;
import com.petmatz.domain.pet.component.PetUpdater;
import com.petmatz.domain.pet.dto.OpenApiPetInfo;
import com.petmatz.domain.pet.dto.PetInf;
import com.petmatz.domain.pet.dto.PetUpdateInfo;
import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.pet.exception.PetErrorCode;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.pet.repository.PetRepository;
import com.petmatz.domain.pet.utils.PetMapper;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.pet.exception.PetErrorCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PetService {

    private final OpenApiPet openApiPet;
    private final PetDelete petDelete;
    private final PetAppend petAppend;
    private final PetUpdater petUpdater;

    //펫 등록번호 조회
    public OpenApiPetInfo fetchPetInfo(String dogRegNo, String ownerNm) {
        return openApiPet.getOpenApiPetInfo(dogRegNo, ownerNm);
    }

    //펫 삭제
    @Transactional
    public void deletePet(Long petId, User user) {
        petDelete.deletePet(petId, user);
    }
    
    // 펫 저장
    @Transactional
    public S3ImgDataInfo savePet(User user, PetInf petInf) throws MalformedURLException {
        return petAppend.savePet(user, petInf);
    }
    
    // 펫 업데이트
    @Transactional
    public S3ImgDataInfo updatePet(Long petId, User user, PetUpdateInfo petUpdateInfo) throws MalformedURLException {
        return petUpdater.updatePet(petId, user, petUpdateInfo);
    }

}


