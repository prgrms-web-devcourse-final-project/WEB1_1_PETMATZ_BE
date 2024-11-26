package com.petmatz.domain.chatting.repository;

import com.petmatz.domain.chatting.dto.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    @Query("select c from ChatRoomEntity c where c.user1 = :user1 and c.user2 = :user2")
    Optional<ChatRoomEntity> selectUser1AndUser2(@Param("user1") String user1,
                                                 @Param("user2") String user2);

    //채팅방을 연쪽, 그 반대쪽 중 하나라도 만족하면 채팅방을 가져옴
    @Query("select c from ChatRoomEntity c where c.user1 = :userEmail or c.user2 = :userEmail")
    Optional<List<ChatRoomEntity>> selectUserInChatRoomList(@Param("userEmail") String userEmail);

}
