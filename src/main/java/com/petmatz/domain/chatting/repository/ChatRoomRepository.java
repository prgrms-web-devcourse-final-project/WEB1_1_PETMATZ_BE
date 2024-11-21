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

    @Query("select c from ChatRoomEntity c where c.user1 = :userName or c.user2 = :userName")
    Optional<List<ChatRoomEntity>> selectUserInChatRoomList(@Param("userName") String userName);
}
