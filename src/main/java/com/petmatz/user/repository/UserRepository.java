package com.petmatz.user.repository;

import com.petmatz.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(Long userId);
    boolean existsByAccountId(String accountId);
    User findById(Long userId);
    User findByAccountId(String accountId);
    User findByAccountIdAndIsDeletedFalse(String accountId); // Only fetch active users
    User findByIdAndIsDeletedFalse(Long id); // Only fetch active use
}
