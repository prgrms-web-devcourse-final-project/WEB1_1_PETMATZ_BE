package com.petmatz.user.repository;

import com.petmatz.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByAccountId(String accountId);
    UserEntity findByAccountId(String accountId);
//    UserEntity findByAccountIdAndIsDeletedFalse(String accountId); // Only fetch active users
//    UserEntity findByIdAndIsDeletedFalse(Long id); // Only fetch active use

}
