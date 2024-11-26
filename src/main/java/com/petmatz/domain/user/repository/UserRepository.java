package com.petmatz.domain.user.repository;

import com.petmatz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long userId);
    boolean existsByAccountId(String accountId);
    Optional<User> findById(Long userId);
    User findByAccountId(String accountId);
}
