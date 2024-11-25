package com.petmatz.domain.user.repository;

import com.petmatz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(Long userId);
    boolean existsByAccountId(String accountId);
    User findById(Long userId);
    User findByAccountId(String accountId);
}
