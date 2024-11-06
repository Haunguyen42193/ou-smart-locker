package com.example.ousmartlocker.repository;

import com.example.ousmartlocker.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
