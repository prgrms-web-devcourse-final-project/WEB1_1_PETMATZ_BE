package com.petmatz.domain.user.repository;

import com.petmatz.domain.user.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByMyIdAndHeartedId(Long myId, Long heartedId);
    void deleteByMyIdAndHeartedId(Long myId, Long heartedId);
    void deleteAllByHeartedId(Long heartedId);
    List<Heart> findAllByMyId(Long myId);
}
