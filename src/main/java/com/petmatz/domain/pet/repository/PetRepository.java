package com.petmatz.domain.pet.repository;


import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByDogRegNo(String dogRegNo);

    Optional<Pet> findByIdAndUser(Long id, User user);

    List<Pet> findByUserId(Long userId); // userId로 Pet 조회

    @Query("select p from Pet p where p.id IN :petList")
    List<Pet> findPetListByPetId(@Param("petList") List<String> petList);


    List<Pet> findAllByUserId(Long userId);
}
