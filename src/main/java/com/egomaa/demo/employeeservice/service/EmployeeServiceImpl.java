package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.exception.DuplicateResourceException;
import com.egomaa.demo.employeeservice.exception.ResourceNotFoundException;
import com.egomaa.demo.employeeservice.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;

    private EmployeeDto mapToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setBirthDate(employee.getBirthDate());
        dto.setSalary(employee.getSalary());
        return dto;
    }

    private Employee mapToEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setBirthDate(dto.getBirthDate());
        employee.setSalary(dto.getSalary());
        return employee;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Attempting to create employee with email: {}", employeeDto.getEmail());
        if (employeeRepo.existsByEmail(employeeDto.getEmail())) {
            log.error("Duplicate email detected: {}", employeeDto.getEmail());
            throw new DuplicateResourceException("Email already exists: " + employeeDto.getEmail());
        }
        Employee saved = employeeRepo.save(mapToEntity(employeeDto));
        log.info("Employee created successfully with ID: {}", saved.getId());
        return mapToDto(saved);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("Searching for employee with ID: {}", id);
        return employeeRepo.findById(id)
                .map(employee -> {
                    log.debug("Employee found: {}", employee);
                    return mapToDto(employee);
                })
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("Updating employee with ID: {}", id);
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        employee.setFullName(employeeDto.getFullName());
        employee.setEmail(employeeDto.getEmail());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setSalary(employeeDto.getSalary());

        Employee updated = employeeRepo.save(employee);
        log.info("Employee updated successfully: {}", updated.getId());
        return mapToDto(updated);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees from database");
        List<EmployeeDto> employees = employeeRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} employees", employees.size());
        return employees;
    }

    @Override
    public void deleteEmployee(Long id) {
        log.warn("Deleting employee with ID: {}", id);
        if (!employeeRepo.existsById(id)) {
            log.error("Cannot delete, employee not found with ID: {}", id);
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepo.deleteById(id);
        log.info("Employee deleted successfully with ID: {}", id);
    }
}
