package com.petmatz.domain.user.repository;

import com.petmatz.domain.user.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByMyIdAndHeartedId(Long myId, Long heartedId);
    List<Heart> findAllByMyId(Long myId);
    Optional<Heart> findByMyIdAndHeartedId(Long id, Long heartedId);
}
