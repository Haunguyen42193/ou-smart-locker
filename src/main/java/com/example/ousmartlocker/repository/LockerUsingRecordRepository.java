package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.dto.LockerUsingDto;
import com.example.ousmartlocker.dto.UserUsingDto;
import com.example.ousmartlocker.model.LockerUsingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LockerUsingRecordRepository extends JpaRepository<LockerUsingRecord, Long> {
    @Query("SELECT COUNT(lr) FROM LockerUsingRecord lr WHERE lr.date >= ?1 AND lr.date <= ?2")
    long countRecordsBetween(Date dateTimeStart, Date dateTimeEnd);

    @Query("SELECT new com.example.ousmartlocker.dto.LockerUsingDto(lr.locker.lockerName, COUNT(lr)) FROM LockerUsingRecord lr WHERE lr.date >= ?1 AND lr.date <= ?2 GROUP BY lr.locker")
    List<LockerUsingDto> countRecordGroupByLocker(Date dateTimeStart, Date dateTimeEnd);

    @Query("SELECT new com.example.ousmartlocker.dto.UserUsingDto(lr.user.name, COUNT(lr)) FROM LockerUsingRecord lr WHERE lr.date >= ?1 AND lr.date <= ?2 GROUP BY lr.user")
    List<UserUsingDto> countRecordGroupByUser(Date dateTimeStart, Date dateTimeEnd);
}
