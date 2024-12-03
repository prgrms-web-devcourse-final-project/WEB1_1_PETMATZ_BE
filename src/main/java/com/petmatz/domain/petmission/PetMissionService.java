package com.petmatz.domain.petmission;

import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.petmission.component.UserToChatRoomReader;
import com.petmatz.domain.petmission.component.UserToPetMissionInserter;
import com.petmatz.domain.petmission.component.UserToPetMissionReader;
import com.petmatz.domain.petmission.dto.PetMissionData;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import com.petmatz.domain.petmission.dto.UserToPetMissionListInfo;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetMissionService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    private final UserToChatRoomReader userToChatRoomReader;
    private final JwtExtractProvider jwtExtractProvider;
    private final UserToPetMissionInserter userToPetMissionInserter;
    private final UserToPetMissionReader userToPetMissionReader;

    public PetMissionData insertPetMission(PetMissionInfo petMissionInfo) {

        Long careId = jwtExtractProvider.findIdFromJwt();

        List<User> users = makeUserEntityList(careId, petMissionInfo.receiverId());
        String chatRoomId = userToChatRoomReader.selectChatRoomId(users.get(0).getAccountId(), users.get(1).getAccountId());

        //TODO 예외처리 해야 함
        Optional<Pet> byId = petRepository.findById(Long.valueOf(petMissionInfo.petId()));
        Pet pet = byId.get();

        PetMissionEntity petMissionEntity = PetMissionEntity.of(petMissionInfo, pet);

        List<UserToPetMissionEntity> list = users.stream().map(
                user -> UserToPetMissionEntity.of(user, petMissionEntity)
        ).toList();

        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionInserter.insertUserToPetMission(list);

        //TODO 반환 DTO 만들기

        return PetMissionData.of(chatRoomId,userToPetMissionEntities);

    }

    public List<UserToPetMissionListInfo> selectPetMissionList() {

        Long userId = jwtExtractProvider.findIdFromJwt();
        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionReader.selectUserToPetMissionList(userId);
        return userToPetMissionEntities.stream().map(
                UserToPetMissionListInfo::of
        ).toList();

    }

    @Transactional
    public void updatePetMissionStatus(PetMissionUpdateRequest petMissionUpdateRequest) {
        jwtExtractProvider.findIdFromJwt();
        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionReader.selectUserToPetMissionList(petMissionUpdateRequest);
        for (UserToPetMissionEntity userToPetMissionEntity : userToPetMissionEntities) {
            PetMissionEntity petMission = userToPetMissionEntity.getPetMission();
            petMission.updatePetMissionStatusZip(petMissionUpdateRequest.missionStatusZip());
        }
    }

    public void selectPetMissionInfo(String petMissionId) {
        Long userId = jwtExtractProvider.findIdFromJwt();

        UserToPetMissionEntity userToPetMissionEntity = userToPetMissionReader.selectUserToPetMission(petMissionId, userId);

    }

    private List<User> makeUserEntityList(Long careId, Long receiverId) {
        ArrayList<User> userList = new ArrayList<>();
         userList.add(userRepository.findById(careId).get());
         userList.add(userRepository.findById(receiverId).get());
        return userList;
    }


}

