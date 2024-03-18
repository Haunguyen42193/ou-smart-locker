package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.LockerOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockerOtpRepository extends JpaRepository<LockerOtp, Long> {
}
