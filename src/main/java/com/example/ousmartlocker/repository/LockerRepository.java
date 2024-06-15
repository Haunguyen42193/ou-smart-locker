package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.model.LockerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockerRepository extends JpaRepository<Locker, Long> {

    @Query("select l from locker l where l.isOccupied = false and l.lockerLocation.locationId = :locationId")
    List<Locker> findByIsOccupiedFalseAndLockerLocation(Long locationId);

    List<Locker> findAllByLockerLocation_LocationId(Long locationId);

}
