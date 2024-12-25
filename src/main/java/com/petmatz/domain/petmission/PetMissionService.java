package com.petmatz.domain.petmission;

import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.aws.vo.S3Imge;
import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.pet.repository.PetRepository;
import com.petmatz.domain.petmission.component.*;
import com.petmatz.domain.petmission.dto.*;
import com.petmatz.domain.petmission.entity.*;
import com.petmatz.domain.petmission.exception.ExistPetMissionAnswerException;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetMissionService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    private final UserToChatRoomReader userToChatRoomReader;
    private final UserToPetMissionInserter userToPetMissionInserter;
    private final UserToPetMissionReader userToPetMissionReader;
    private final PetMissionReader petMissionReader;
    private final PetMissionInserter petMissionInserter;
    private final PetMissionAskReader petMissionAskReader;
    private final AwsClient awsClient;


    public String selectChatRoomId(String careEmail, String receiverEmail) {
        return userToChatRoomReader.selectChatRoomId(careEmail, receiverEmail);
    }
    @Transactional
    public PetMissionData insertPetMission(PetMissionInfo petMissionInfo, Long careId) {

        List<User> users = makeUserEntityList(careId, petMissionInfo.receiverId());

        String chatRoomId = userToChatRoomReader.selectChatRoomId(users.get(0).getAccountId(), users.get(1).getAccountId());

        List<Pet> pets = petRepository.findPetListByPetId(petMissionInfo.petId());
        if (pets.isEmpty()) {
            throw new IllegalArgumentException("해당 Pet ID에 대한 펫을 찾을 수 없습니다");
        }

        List<PetMissionAskEntity> petMissionAskEntityList = petMissionInfo.petMissionAskInfo()
                .stream()
                .map(PetMissionAskEntity::of)
                .toList();

        PetMissionEntity petMissionEntity = PetMissionEntity.of(petMissionInfo);
        petMissionEntity.addPetMissionAsk(petMissionAskEntityList);

        List<PetToPetMissionEntity> petToPetMissionEntities = pets.stream()
                .map(pet -> PetToPetMissionEntity.of(pet, petMissionEntity))
                .toList();

        petToPetMissionEntities.forEach(petMissionEntity::addPetToPetMission);

        List<UserToPetMissionEntity> userToPetMissionEntities = users.stream()
                .map(user -> UserToPetMissionEntity.of(user, petMissionEntity, careId))
                .toList();

        userToPetMissionInserter.insertUserToPetMission(userToPetMissionEntities);
        return PetMissionData.of(chatRoomId, petMissionEntity);
    }

    public List<UserToPetMissionEntity> selectPetMissionList(Long userId) {
        return userToPetMissionReader.selectUserToPetMissionList(userId);
    }

    public List<UserToPetMissionEntity> selectPetMissionList(Long userId, LocalDate petMissionStart) {
        return userToPetMissionReader.selectUserToPetMissionList(userId, petMissionStart);
    }

    @Transactional
    public void updatePetMissionStatus(PetMissionUpdateRequest petMissionUpdateRequest) {
        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionReader.selectUserToPetMissionList(petMissionUpdateRequest);
        for (UserToPetMissionEntity userToPetMissionEntity : userToPetMissionEntities) {
            PetMissionEntity petMission = userToPetMissionEntity.getPetMission();
            petMission.updatePetMissionStatusZip(petMissionUpdateRequest.missionStatusZip());
        }
    }

    public List<UserToPetMissionEntity> selectUserToPetMissionList(String petMissionId) {
        return userToPetMissionReader.selectUserToPetMissionList(petMissionId);
    }

    public PetMissionDetails selectPetMissionInfo(String petMissionId) {
        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionReader.selectUserToPetMissionList(petMissionId);

        PetMissionEntity petMissionEntity = petMissionReader.selectUserToPetMission(petMissionId);

        return PetMissionDetails.of(petMissionEntity, userToPetMissionEntities);
    }

    private List<User> makeUserEntityList(Long careId, Long receiverId) {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(userRepository.findById(careId).get());
        userList.add(userRepository.findById(receiverId).get());
        return userList;
    }


    @Transactional
    public String updatePetMissionComment(PetMissionCommentInfo petMissionCommentInfo, String userEmail) throws MalformedURLException {
        PetMissionAskEntity petMissionAskEntity = petMissionReader.selectById(Long.valueOf(petMissionCommentInfo.askId()));
        if (petMissionAskEntity.getMissionAnswer() != null) {
            throw ExistPetMissionAnswerException.EXCEPTION;
        }

        S3Imge petImg = awsClient.UploadImg(userEmail, petMissionCommentInfo.imgURL(), "CARE_HISTORY_IMG", String.valueOf(petMissionAskEntity.getId()));

        //6-1 Img 정제
        PetMissionAnswerEntity petMissionAnswerEntity = petMissionInserter.insertPetMissionAnswer(PetMissionAnswerEntity.of(petMissionCommentInfo, petImg.uploadURL()));
        petMissionAskEntity.addPetMissionAnswer(petMissionAnswerEntity);
        return petImg.checkResultImg();
    }

    public PetMissionAnswerInfo selectPetMissionAnswerInfo(String askId) {
        PetMissionAskEntity petMissionAskEntity = petMissionAskReader.selectPetMissionAskInfo(askId);
        if (petMissionAskEntity.getMissionAnswer() == null) {
            return PetMissionAnswerInfo.builder().build();
        }
        return petMissionAskEntity.getMissionAnswer().of();
    }
}

