package com.petmatz.domain.sosboard;


import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sos_board")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SosBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "sosBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetSosBoard> petSosBoards = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType; // 시급/일급/협의 중 선택

    @Column(name = "price")
    private Integer price; // 금액 (시급/일급의 경우 필수, 협의는 null 허용)

    @Column(name = "start_date", nullable = false)
    private String startDate; // yyyy-MM-dd HH:mm 형식

    @Column(name = "end_date", nullable = false)
    private String endDate; // yyyy-MM-dd HH:mm 형식

    public void addPetSosBoards(List<PetSosBoard> petSosBoards) {
        if (this.petSosBoards == null) {
            this.petSosBoards = new ArrayList<>();
        }
        this.petSosBoards.addAll(petSosBoards);
        // 양방향 연관 관계 설정
        petSosBoards.forEach(petSosBoard -> petSosBoard.setSosBoard(this));
    }

    public void updateFields(String title, PaymentType paymentType, Integer price, String comment,
                       String startDate, String endDate) {
        this.title = title;
        this.paymentType = paymentType;
        this.price = price;
        this.comment = comment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void clearPetSosBoards() {
        this.petSosBoards.clear(); // 기존 리스트 비우기
    }
}
