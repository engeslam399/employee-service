package com.egomaa.demo.employeeservice.controller;

import com.egomaa.demo.employeeservice.constants.AppConstants;
import com.egomaa.demo.employeeservice.dto.ApiResponse;
import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(AppConstants.EMPLOYEES_API)
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(
            @Valid @RequestBody EmployeeDto employeeDto) {

        return null;
    }
}

