package com.egomaa.demo.employeeservice.controller;

import com.egomaa.demo.employeeservice.dto.ApiResponse;
import com.egomaa.demo.employeeservice.dto.request.CreateEmployeeRequest;
import com.egomaa.demo.employeeservice.dto.response.CreateEmployeeResponse;
import com.egomaa.demo.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateEmployeeResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest createEmployeeRequest) {

        ApiResponse<CreateEmployeeResponse> apiResponse = new ApiResponse<>();

        CreateEmployeeResponse responseData = employeeService.createEmployee(createEmployeeRequest);
        apiResponse.setStatus(String.valueOf(HttpStatus.CREATED.value()));
        apiResponse.setMessage("Employee created successfully and onboarding initiated.");
        apiResponse.setData(responseData);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }




}

