package com.petmatz.domain.match.repo;

import com.petmatz.domain.match.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 7km 이내의 사용자 1000명 사각형 범위로 러프하게 먼저 필터링
 */
@Repository
public interface MatchUserRepository extends JpaRepository<User, Long> {

    @Query(value = """
           SELECT id, latitude, longitude, is_care_available, preferred_size, mbti
           FROM users
           WHERE latitude BETWEEN :latMin AND :latMax
             AND longitude BETWEEN :lngMin AND :lngMax
           LIMIT 1000;
           """, nativeQuery = true)
    List<Object[]> findUsersWithinBoundingBox(@Param("latMin") double latMin,
                                              @Param("latMax") double latMax,
                                              @Param("lngMin") double lngMin,
                                              @Param("lngMax") double lngMax);
}
