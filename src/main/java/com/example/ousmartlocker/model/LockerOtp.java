package com.example.ousmartlocker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LockerOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lockerOtpId;
    @ManyToOne
    @JsonBackReference
    private Otp otp;
    @ManyToOne
    @JsonBackReference
    private Locker locker;
}
