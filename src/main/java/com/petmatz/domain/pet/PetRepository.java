package com.petmatz.domain.pet;

import com.petmatz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByDogRegNo(String dogRegNo);

    Optional<Pet> findByIdAndUser(Long id, User user);
}
