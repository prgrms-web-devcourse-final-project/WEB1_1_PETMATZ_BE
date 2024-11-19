package com.petmatz.domain.global;//package spring.basic.domain.global;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.MappedSuperclass;
//import lombok.Getter;
//import org.springframework.data.annotation.CreatedBy;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedBy;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//@MappedSuperclass
//@Getter
//@EntityListeners(AuditingEntityListener.class)
//public abstract class BaseEntity {
//
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createAt;
//
//    @LastModifiedDate
//    private LocalDateTime updateAt;
//
//    @CreatedBy
//    @Column(updatable = false)
//    private String createdBy;
//
//    @LastModifiedBy
//    private String lastModifiedBy;
//
//}
