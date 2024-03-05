package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.PassResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassResetOtpRepository extends JpaRepository<PassResetOtp, Long> {
    PassResetOtp findByOtp(String otp);
}
