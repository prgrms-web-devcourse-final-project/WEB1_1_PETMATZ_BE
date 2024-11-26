package com.petmatz.domain.chatting.repository;

import com.petmatz.domain.chatting.dto.UserToChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserToChatRoomRepository extends JpaRepository<UserToChatRoomEntity, Long> {

    Optional<List<UserToChatRoomEntity>> findByUser_AccountId(String userEmail);
}
