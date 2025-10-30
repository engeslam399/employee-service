package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.dto.request.CreateEmployeeRequest;
import com.egomaa.demo.employeeservice.dto.response.CreateEmployeeResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface EmployeeService {


    CreateEmployeeResponse createEmployee(@Valid CreateEmployeeRequest createEmployeeRequest);
}
