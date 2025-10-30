package com.egomaa.demo.employeeservice.repo;

import com.egomaa.demo.employeeservice.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByCode(String code);
}
