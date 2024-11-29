package com.petmatz.domain.user.repository;

import com.petmatz.domain.user.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Certification findTopByAccountIdOrderByCreatedAtDesc(String accountId);
    void deleteAllByAccountId(String accountId);
}
