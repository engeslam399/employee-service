package com.egomaa.demo.employeeservice.controller;

import com.egomaa.demo.employeeservice.constants.AppConstants;
import com.egomaa.demo.employeeservice.dto.ApiResponse;
import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.EMPLOYEES_API)
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(
            @Valid @RequestBody EmployeeDto employeeDto) {
        log.info("Request to create employee with email: {}", employeeDto.getEmail());
        EmployeeDto created = employeeService.createEmployee(employeeDto);
        log.info("Employee created successfully with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", AppConstants.EMPLOYEE_CREATED, created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee with ID: {}", id);
        EmployeeDto employee = employeeService.getEmployeeById(id);
        log.debug("Employee details retrieved: {}", employee);
        return ResponseEntity.ok(
                new ApiResponse<>("success", AppConstants.EMPLOYEE_RETRIEVED, employee));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployees() {
        log.info("Fetching all employees");
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        log.info("Total employees found: {}", employees.size());
        return ResponseEntity.ok(
                new ApiResponse<>("success", AppConstants.EMPLOYEES_RETRIEVED, employees));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDto employeeDto) {
        log.info("Updating employee with ID: {}", id);
        EmployeeDto updated = employeeService.updateEmployee(id, employeeDto);
        log.info("Employee updated successfully with ID: {}", id);
        return ResponseEntity.ok(
                new ApiResponse<>("success", AppConstants.EMPLOYEE_UPDATED, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        log.warn("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        log.info("Employee deleted successfully with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>("success", AppConstants.EMPLOYEE_DELETED, null));
    }
}
