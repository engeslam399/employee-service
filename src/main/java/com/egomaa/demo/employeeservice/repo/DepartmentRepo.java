package com.egomaa.demo.employeeservice.repo;

import com.egomaa.demo.employeeservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department,Long> {
}
