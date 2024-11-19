package com.petmatz.user.repository;

import com.petmatz.user.entity.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, String> {
    CertificationEntity findByAccountId(String accountId);
    void deleteByAccountId(String accountId);
    CertificationEntity findTopByAccountIdOrderByCreatedAtDesc(String accountId);
}
