package com.egomaa.demo.employeeservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateEmployeeRequest {

    private String firstName;
    private String lastName;
    private String personalEmail;
    private LocalDate hireDate;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Long departmentId;
    private Long positionId;
    private Long managerId;

}
