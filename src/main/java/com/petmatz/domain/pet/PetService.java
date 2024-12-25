package com.petmatz.domain.pet;

import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.aws.vo.S3Imge;
import com.petmatz.domain.global.S3ImgDataInfo;
import com.petmatz.domain.pet.component.OpenApiPet;
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

    private final AwsClient awsClient;
    private final OpenApiPet openApiPet;

    private final PetRepository repository;
    private final UserRepository userRepository;


    public OpenApiPetInfo fetchPetInfo(String dogRegNo, String ownerNm) {
        return openApiPet.getOpenApiPetInfo(dogRegNo, ownerNm);
    }

    //TODO Component로 옮기기
    // 펫 삭제
    public void deletePet(Long petId, User user) {
        repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PET_NOT_FOUND));
        repository.deleteById(petId);
//        repository.delete(pet);
    }


    // 펫 저장
    public S3ImgDataInfo savePet(User user, PetInf petInf) throws MalformedURLException {
        if (repository.existsByDogRegNo(petInf.dogRegNo())) {
            throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
        }

        //6-1 Img 정제
        S3Imge petImg = awsClient.UploadImg(user.getAccountId(), petInf.profileImg(), "PET_IMG", petInf.dogRegNo());
        //Pet Entity 생성
        Pet pet = PetMapper.of(petInf, petImg.uploadURL(), user);

        //Pet 정보 저장
        Pet petEntity = repository.save(pet);
        Long id = petEntity.getId();

        // User의 isRegistered 상태 업데이트
        user.updateUserRegistered();

//        return S3ImgDataInfo.of(id, resultImgURL);
        return S3ImgDataInfo.of(id, petImg.checkResultImg());
    }


    // 펫 업데이트
    @Transactional
    public S3ImgDataInfo updatePet(Long petId, User user, PetUpdateInfo petUpdateInfo) throws MalformedURLException {
        Pet existingPet = repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));

        // 현재 사용자가 리소스 소유자인지 검증
        if (!existingPet.getUser().equals(user)) {
            throw new SecurityException("권한이 없습니다.");
        }

        S3Imge petImg = awsClient.UploadImg(user.getAccountId(), petUpdateInfo.profileImg(), "PET_IMG", existingPet.getDogRegNo());

        // 병합된 DTO를 기반으로 엔티티 생성
        String resultImg = existingPet.updateImgURL(petUpdateInfo.profileImg(), petImg);
        existingPet.updatePetInfo(petUpdateInfo);

        return S3ImgDataInfo.of(petId, resultImg);
    }

    //아래 리펙은 상당히 힘들지도

    public List<Pet> getPetsByUserId(Long userId) {
        List<Pet> userPets = repository.findByUserId(userId);
        if (userPets.isEmpty()) {
            throw new PetServiceException(PET_NOT_FOUND);
        }
        return userPets;
    }

    public List<String> getTemperamentsByUserId(Long userId) {
        List<Pet> pets = repository.findByUserId(userId);
        if (pets.isEmpty()) {
            throw new RuntimeException();
        }
        return pets.stream()
                .map(pet -> pet.getTemperament() != null ? pet.getTemperament() : "Unknown")
                .collect(Collectors.toList());
    }
}


