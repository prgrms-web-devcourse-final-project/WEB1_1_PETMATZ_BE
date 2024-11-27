package com.petmatz.domain.match.repo;

import com.petmatz.domain.user.entity.User;
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

            SELECT id, latitude, longitude, is_care_available, preferred_size, mbti,
              ST_Distance_Sphere(
                  point(RADIANS(longitude), RADIANS(latitude)), 
                  point(RADIANS(:userLng), RADIANS(:userLat))
              ) AS distance_in_meters
       FROM user
       WHERE latitude BETWEEN :minLat AND :maxLat
       AND longitude BETWEEN :minLng AND :maxLng
       ORDER BY distance_in_meters ASC
       LIMIT 1000;
       """, nativeQuery = true)
    List<Object[]> findUsersWithinBoundingBox(@Param("userLng") double userLng,
                                              @Param("userLat") double userLat,
                                              @Param("minLat") double minLat,
                                              @Param("maxLat") double maxLat,
                                              @Param("minLng") double minLng,
                                              @Param("maxLng") double maxLng);
}