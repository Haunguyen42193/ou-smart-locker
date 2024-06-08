package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.dto.LoginRecordUserDto;
import com.example.ousmartlocker.model.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

    @Query("SELECT COUNT(lr) FROM LoginRecord lr WHERE lr.loginTime >= ?1 AND lr.loginTime <= ?2")
    long countLoginRecordsBetween(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query("SELECT new com.example.ousmartlocker.dto.LoginRecordUserDto(lr.username, COUNT(lr)) FROM LoginRecord lr WHERE lr.loginTime >= ?1 AND lr.loginTime <= ?2 GROUP BY lr.username")
    List<LoginRecordUserDto> countLoginsByUsername(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
