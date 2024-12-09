package com.petmatz.domain.user.repository;

import com.petmatz.domain.user.entity.Heart;
import com.petmatz.domain.user.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>{
    Boolean existsByMyIdAndRecommendedId(Long myId, Long recommendedId);
}
