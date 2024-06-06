package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

    @Query("SELECT COUNT(lr) FROM LoginRecord lr WHERE lr.loginTime BETWEEN :startTime AND :endTime")
    long countLoginRecordsBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
