package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
