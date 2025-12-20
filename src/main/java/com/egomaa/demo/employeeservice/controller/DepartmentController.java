package com.egomaa.demo.employeeservice.controller;

import com.egomaa.demo.employeeservice.constants.AppConstants;
import com.egomaa.demo.employeeservice.dto.ApiResponse;
import com.egomaa.demo.employeeservice.dto.DepartmentDto;
import com.egomaa.demo.employeeservice.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.DEPARTMENTS_API)
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentDto>> createDepartment(
            @Valid @RequestBody DepartmentDto departmentDto) {
        log.info("Request to create department with name: {}", departmentDto.getName());
        DepartmentDto created = departmentService.createDepartment(departmentDto);
        log.info("Department created successfully with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", AppConstants.DEPARTMENT_CREATED, created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable Long id) {
        log.info("Fetching department with ID: {}", id);
        DepartmentDto department = departmentService.getDepartmentById(id);
        log.debug("Department details retrieved: {}", department);
        return ResponseEntity.ok(
                new ApiResponse<>("success", AppConstants.DEPARTMENT_RETRIEVED, department));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getAllDepartments() {
        log.info("Fetching all departments");
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        log.info("Total departments found: {}", departments.size());
        return ResponseEntity.ok(
                new ApiResponse<>("success", AppConstants.DEPARTMENTS_RETRIEVED, departments));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDto departmentDto) {
        log.info("Updating department with ID: {}", id);
        DepartmentDto updated = departmentService.updateDepartment(id, departmentDto);
        log.info("Department updated successfully with ID: {}", id);
        return ResponseEntity.ok(
                new ApiResponse<>("success", AppConstants.DEPARTMENT_UPDATED, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        log.warn("Deleting department with ID: {}", id);
        departmentService.deleteDepartment(id);
        log.info("Department deleted successfully with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>("success", AppConstants.DEPARTMENT_DELETED, null));
    }
}
