package com.egomaa.demo.employeeservice.dto;

import com.egomaa.demo.employeeservice.entity.Department;
import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.entity.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class EmployeeDto {

    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String personalEmail;
    private LocalDate hireDate;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Double salary;
    private Long departmentId;
    private Long positionId;
    private Long managerId;

}