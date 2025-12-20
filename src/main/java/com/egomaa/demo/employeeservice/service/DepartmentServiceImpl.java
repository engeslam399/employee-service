package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.dto.DepartmentDto;
import com.egomaa.demo.employeeservice.entity.Department;
import com.egomaa.demo.employeeservice.exception.DuplicateResourceException;
import com.egomaa.demo.employeeservice.exception.ResourceNotFoundException;
import com.egomaa.demo.employeeservice.repo.DepartmentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;

    private DepartmentDto mapToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        return dto;
    }

    private Department mapToEntity(DepartmentDto dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        return department;
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        log.info("Attempting to create department with name: {}", departmentDto.getName());
        if (departmentRepo.existsByName(departmentDto.getName())) {
            log.error("Duplicate department name detected: {}", departmentDto.getName());
            throw new DuplicateResourceException("Department name already exists: " + departmentDto.getName());
        }
        Department saved = departmentRepo.save(mapToEntity(departmentDto));
        log.info("Department created successfully with ID: {}", saved.getId());
        return mapToDto(saved);
    }

    @Override
    public DepartmentDto getDepartmentById(Long id) {
        log.info("Searching for department with ID: {}", id);
        return departmentRepo.findById(id)
                .map(department -> {
                    log.debug("Department found: {}", department);
                    return mapToDto(department);
                })
                .orElseThrow(() -> {
                    log.error("Department not found with ID: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        log.info("Fetching all departments from database");
        List<DepartmentDto> departments = departmentRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} departments", departments.size());
        return departments;
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        log.info("Updating department with ID: {}", id);
        Department department = departmentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Department not found with ID: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });

        // Check if the new name conflicts with another department
        if (!department.getName().equals(departmentDto.getName()) &&
                departmentRepo.existsByName(departmentDto.getName())) {
            log.error("Cannot update: duplicate department name: {}", departmentDto.getName());
            throw new DuplicateResourceException("Department name already exists: " + departmentDto.getName());
        }

        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        Department updated = departmentRepo.save(department);
        log.info("Department updated successfully: {}", updated.getId());
        return mapToDto(updated);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.warn("Deleting department with ID: {}", id);
        if (!departmentRepo.existsById(id)) {
            log.error("Cannot delete, department not found with ID: {}", id);
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepo.deleteById(id);
        log.info("Department deleted successfully with ID: {}", id);
    }
}
