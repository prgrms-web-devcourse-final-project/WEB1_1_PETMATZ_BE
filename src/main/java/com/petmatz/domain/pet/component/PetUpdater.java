package com.petmatz.domain.pet.component;

import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.aws.vo.S3Imge;
import com.petmatz.domain.global.S3ImgDataInfo;
import com.petmatz.domain.pet.dto.PetUpdateInfo;
import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.pet.exception.PetErrorCode;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.pet.repository.PetRepository;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;

@Component
@RequiredArgsConstructor
public class PetUpdater {

    private final AwsClient awsClient;
    private final PetRepository petRepository;

    public S3ImgDataInfo updatePet(Long petId, User user, PetUpdateInfo petUpdateInfo) throws MalformedURLException {
        Pet existingPet = petRepository.findByIdAndUser(petId, user)
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

}
