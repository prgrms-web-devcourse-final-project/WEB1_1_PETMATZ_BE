package com.petmatz.domain.sosboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SosBoardRepository extends JpaRepository<SosBoard, Long> {

    // 지역 필터링 (User의 region 기준)
    @Query("SELECT s FROM SosBoard s JOIN s.user u WHERE u.region = :region")
    Page<SosBoard> findByUserRegion(@Param("region") String region, Pageable pageable);


    // 인덱스 기반 전체 조회 (id 기준 내림차순)
    List<SosBoard> findAllByIdLessThanOrderByIdDesc(Long lastIndex, Pageable pageable);

    // 지역 필터링 + 인덱스 기반 조회 (User의 region 기준)
    @Query("SELECT s FROM SosBoard s JOIN s.user u WHERE u.region = :region AND s.id < :lastIndex ORDER BY s.id DESC")
    List<SosBoard> findByUserRegionAndIdLessThanOrderByIdDesc(@Param("region") String region, @Param("lastIndex") Long lastIndex, Pageable pageable);

    // 특정 사용자의 닉네임 + 지역 기반 조회
    @Query("SELECT s FROM SosBoard s JOIN s.user u WHERE u.nickname = :nickname AND u.region = :region")
    List<SosBoard> findByUserNicknameAndRegion(@Param("nickname") String nickname, @Param("region") String region, Pageable pageable);

    // 특정 사용자 ID 기반 게시글 조회
    List<SosBoard> findByUserId(Long userId);

    // 닉네임 기반 게시글 조회 (id 내림차순 정렬)
    Page<SosBoard> findByUserNickname(String nickname, Pageable pageable);
}

