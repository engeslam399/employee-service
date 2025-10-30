package com.egomaa.demo.employeeservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class CreateEmployeeResponse {

    private String employeeCode;
    private String fullName;
    private String email;
}
