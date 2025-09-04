package com.egomaa.demo.employeeservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class EmployeeDto {
    private Long id;

    private String fullName;
    private String email;
    private LocalDate birthDate;
    private Double salary;
    private String departmentName;
}
