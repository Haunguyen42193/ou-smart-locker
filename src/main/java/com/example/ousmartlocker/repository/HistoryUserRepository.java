package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.HistoryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryUserRepository extends JpaRepository<HistoryUser, Long> {
}
