package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.exception.ResourceNotFoundException;
import com.egomaa.demo.employeeservice.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
        Employee saved = employeeRepo.save(mapToEntity(employeeDto));
        return mapToDto(saved);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepo.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employee.setFullName(employeeDto.getFullName());
        employee.setEmail(employeeDto.getEmail());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setSalary(employeeDto.getSalary());

        return mapToDto(employeeRepo.save(employee));
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepo.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }


}
