package com.petmatz.domain.sosboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SosBoardRepository extends JpaRepository<SosBoard, Long> {
    Page<SosBoard> findByRegion(String region, Pageable pageable);
    // 인덱스 기반 전체 조회 (id 기준 내림차순)
    List<SosBoard> findAllByIdLessThanOrderByIdDesc(Long lastIndex, Pageable pageable);

    // 지역 필터링 + 인덱스 기반 조회 (id 기준 내림차순)
    List<SosBoard> findByRegionAndIdLessThanOrderByIdDesc(String region, Long lastIndex, Pageable pageable);

    // 특정 사용자의 닉네임 기반 조회 (닉네임 + 지역 필터링)
    @Query("SELECT s FROM SosBoard s JOIN s.user u WHERE u.nickname = :nickname AND s.region = :region")
    List<SosBoard> findByUserNicknameAndRegion(@Param("nickname") String nickname, @Param("region") String region, Pageable pageable);

    // 특정 사용자 ID 기반 게시글 조회
    List<SosBoard> findByUserId(Long userId);

    // 특정 지역에서 게시글 수 조회
    long countByRegion(String region);

    // 닉네임 기반 게시글 조회 (id 내림차순 정렬)
    @Query("SELECT s FROM SosBoard s JOIN s.user u WHERE u.nickname = :nickname ORDER BY s.id DESC")
    List<SosBoard> findByUserNickname(@Param("nickname") String nickname, Pageable pageable);
}

