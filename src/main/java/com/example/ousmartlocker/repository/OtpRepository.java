package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByOtpNumber(String otp);
}
