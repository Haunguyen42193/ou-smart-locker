package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.LockerOtp;
import com.example.ousmartlocker.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LockerOtpRepository extends JpaRepository<LockerOtp, Long> {
    Optional<LockerOtp> findByOtp(Otp otp);
}
