package com.petmatz.domain.sosboard;

import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


//Pet, SosBoard 중간테이블
@Entity
@Table(name = "pet_sos_board")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PetSosBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sos_board_id", nullable = false)
    private SosBoard sosBoard;
}
