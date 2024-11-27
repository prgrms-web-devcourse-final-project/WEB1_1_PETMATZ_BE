package com.petmatz.domain.petmission;

import com.petmatz.domain.chatting.repository.UserToChatRoomRepository;
import com.petmatz.domain.petmission.component.PetMissionInseart;
import com.petmatz.domain.petmission.component.UserToChatRoomReader;
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
    private final UserToChatRoomReader userToChatRoomReader;
    private final PetMissionInseart petMissionInseart;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void insertPetMission(PetMissionInfo petMissionInfo) {
        List<User> users = makeUserEntityList(petMissionInfo.careId(), petMissionInfo.receiverId());

        String careEmail = users.get(0).getAccountId();
        String receiverEmail = users.get(1).getAccountId();

        String chatRoomId = userToChatRoomReader.selectChatRoomId(careEmail, receiverEmail);

        List<PetMissionEntity> petMissionEntityList = makePetMissionEntityList(users, petMissionInfo);
        petMissionInseart.insertPetMission(petMissionEntityList);

    }

    private List<User> makeUserEntityList(Long careId, Long receiverId) {
        ArrayList<User> userList = new ArrayList<>();
         userList.add(userRepository.findById(careId).get());
         userList.add(userRepository.findById(receiverId).get());
        return userList;
    }

    private List<PetMissionEntity> makePetMissionEntityList(List<User> users,PetMissionInfo petMissionInfo) {
        List<PetMissionEntity> petMissionEntityList = new ArrayList<>();
        for (User user : users) {
            petMissionEntityList.add(PetMissionEntity.of(user, petMissionInfo));
        }
        return petMissionEntityList;
    }
}
