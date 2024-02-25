package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.entity.Locker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockerRepository extends JpaRepository<Locker, Long> {

    @Query("select l from locker l where l.IsOccupied = false ")
    List<Locker> findByIsOccupiedFalse();

}