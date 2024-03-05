package com.example.ousmartlocker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassResetOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;
    private String setGeneratedAt;
    private String expireTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}