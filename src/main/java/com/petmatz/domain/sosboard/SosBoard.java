package com.petmatz.domain.sosboard;


import com.petmatz.domain.user.entity.User;
import jakarta.persistence.GeneratedValue;
import com.petmatz.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sos_board")
@Getter
@Setter
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

    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "start_date", nullable = false)
    private String startDate; // yyyy-MM-dd HH:mm 형식

    @Column(name = "end_date", nullable = false)
    private String endDate; // yyyy-MM-dd HH:mm 형식

    public enum PaymentType {
        HOURLY, DAILY, NEGOTIABLE;

        public static PaymentType fromString(String value) {
            return switch (value.toUpperCase()) {
                case "HOURLY" -> HOURLY;
                case "DAILY" -> DAILY;
                case "NEGOTIABLE" -> NEGOTIABLE;
                default -> throw new IllegalArgumentException("잘못된 결제 유형: " + value);
            };
        }
    }

}
