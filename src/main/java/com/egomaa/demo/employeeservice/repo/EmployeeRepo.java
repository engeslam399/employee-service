package com.egomaa.demo.employeeservice.repo;

import com.egomaa.demo.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT MIN(e.id) FROM Employee e")
    long findMinId();

    @Query("SELECT MAX(e.id) FROM Employee e")
    long findMaxId();

    Page<Employee> findByIdBetween(long minId, long maxId, Pageable pageable);
}
