package com.egomaa.demo.employeeservice.repo;

import com.egomaa.demo.employeeservice.entity.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByPersonalEmail(String personalEmail);

    /**
     * FIX: Uses a JPQL @Query to find the maximum employeeCode that starts with the
     * provided searchPrefix (e.g., "DEV-25-"). This prevents the "No property 'year'" error.
     */
    @Query("SELECT MAX(e.employeeCode) FROM Employee e WHERE e.employeeCode LIKE :searchPrefix%")
    String findMaxEmployeeCodeByYearAndDepartment(@Param("searchPrefix") String searchPrefix);



}
