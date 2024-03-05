package com.example.ousmartlocker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;
    private String otpNumber;
    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;
    private String setGeneratedAt;
    private String expireTime;
}
