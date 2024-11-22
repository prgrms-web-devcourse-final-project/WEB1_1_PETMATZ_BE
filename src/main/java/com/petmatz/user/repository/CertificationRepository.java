package com.petmatz.user.repository;

import com.petmatz.user.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {
    void deleteByAccountId(String accountId);
    Certification findTopByAccountIdOrderByCreatedAtDesc(String accountId);
}
