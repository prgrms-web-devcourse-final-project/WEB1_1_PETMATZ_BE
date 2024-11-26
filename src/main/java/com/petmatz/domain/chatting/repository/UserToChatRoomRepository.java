package com.petmatz.domain.chatting.repository;

import com.petmatz.domain.chatting.dto.UserToChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserToChatRoomRepository extends JpaRepository<UserToChatRoomEntity, Long> {

    Optional<List<UserToChatRoomEntity>> findByUser_AccountId(String userEmail);

    void deleteByChatRoom_IdAndUser_AccountId(Long chatRoomId, String userEmail);

    @Query("select u.user.accountId from UserToChatRoomEntity u where u.chatRoom.id = :chatRoomId")
    Optional<List<String>> selectChatRoomForUserList(@Param("chatRoomId") Long chatRoomId);
}
