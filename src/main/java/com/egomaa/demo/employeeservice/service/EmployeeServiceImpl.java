package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.exception.DuplicateResourceException;
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



}
