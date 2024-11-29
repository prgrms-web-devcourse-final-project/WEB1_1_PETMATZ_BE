package com.petmatz.domain.petmission;

import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.petmission.component.PetMissionInseart;
import com.petmatz.domain.petmission.component.PetMissionReader;
import com.petmatz.domain.petmission.component.UserToChatRoomReader;
import com.petmatz.domain.petmission.dto.PetMissionData;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetMissionService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    private final UserToChatRoomReader userToChatRoomReader;
    private final PetMissionInseart petMissionInseart;
    private final PetMissionReader petMissionReader;
    private final JwtExtractProvider jwtExtractProvider;

    private final SimpMessagingTemplate simpMessagingTemplate;

//    public List<> selectPetMissionList(Long userUUID) {
////        jwtExtractProvider.findUserId();
//        return petMissionReader.selectPetMissionId(userUUID);
//    }

    public PetMissionData insertPetMission(PetMissionInfo petMissionInfo) {
        //이메일 탐색
//        String accountIdFromJwt = jwtExtractProvider.findAccountIdFromJwt();

        List<User> users = makeUserEntityList(petMissionInfo.careId(), petMissionInfo.receiverId());

        String careEmail = users.get(0).getAccountId();
        String receiverEmail = users.get(1).getAccountId();

        String chatRoomId = userToChatRoomReader.selectChatRoomId(careEmail, receiverEmail);

        //TODO 예외처리 해야 함
        Optional<Pet> byId = petRepository.findById(Long.valueOf(petMissionInfo.petId()));
        Pet pet = byId.get();

        List<PetMissionEntity> petMissionEntityList = petMissionInseart.insertPetMission(makePetMissionEntityList(users, petMissionInfo, pet));

        //TODO 반환 DTO 만들기

        return PetMissionData.of(chatRoomId, petMissionEntityList.stream().map(PetMissionEntity::getId).toList());

    }

    public void selectPetMissionList(Long userUUID) {
        List<PetMissionEntity> petMissionEntityList = petMissionReader.selectPetMissionId(userUUID);

    }

    private List<User> makeUserEntityList(Long careId, Long receiverId) {
        ArrayList<User> userList = new ArrayList<>();
         userList.add(userRepository.findById(careId).get());
         userList.add(userRepository.findById(receiverId).get());
        return userList;
    }

    private List<PetMissionEntity> makePetMissionEntityList(List<User> users,PetMissionInfo petMissionInfo, Pet pet) {
        List<PetMissionEntity> petMissionEntityList = new ArrayList<>();
        for (User user : users) {
            petMissionEntityList.add(PetMissionEntity.of(user, petMissionInfo, pet));
        }
        return petMissionEntityList;
    }
}
