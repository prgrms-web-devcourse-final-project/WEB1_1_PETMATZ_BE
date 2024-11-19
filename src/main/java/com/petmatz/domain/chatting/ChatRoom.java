package com.petmatz.domain.chatting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;

@Entity
public class ChatRoom {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "cr_id")
    private String id;





}
