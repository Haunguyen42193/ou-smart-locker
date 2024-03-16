package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.LockerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockerLocationRepository extends JpaRepository<LockerLocation, Long> {
}
