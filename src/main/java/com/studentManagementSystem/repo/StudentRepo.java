package com.studentManagementSystem.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.studentManagementSystem.entity.Students;

import java.util.List;

public interface StudentRepo extends JpaRepository<Students, Long> {
    Page<Students> findAllByActive(boolean active, Pageable pageable);
    List<Students> findAllByActive(boolean active);
    long countAllByActive(boolean active);
    boolean existsByEmailIgnoreCase(String email);
}
