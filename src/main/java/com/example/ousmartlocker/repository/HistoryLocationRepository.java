package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.HistoryLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryLocationRepository extends JpaRepository<HistoryLocation, Long> {
}
