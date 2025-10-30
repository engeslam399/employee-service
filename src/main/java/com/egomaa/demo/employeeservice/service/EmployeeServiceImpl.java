package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.dto.EmployeeDto;
import com.egomaa.demo.employeeservice.dto.request.CreateEmployeeRequest;
import com.egomaa.demo.employeeservice.dto.response.CreateEmployeeResponse;
import com.egomaa.demo.employeeservice.entity.Department;
import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.entity.Position;
import com.egomaa.demo.employeeservice.exception.DuplicateResourceException;
import com.egomaa.demo.employeeservice.exception.ResourceNotFoundException;
import com.egomaa.demo.employeeservice.repo.DepartmentRepo;
import com.egomaa.demo.employeeservice.repo.EmployeeRepo;
import com.egomaa.demo.employeeservice.repo.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepository;
    private final PositionRepository positionRepository;
    private static final String COMPANY_EMAIL_DOMAIN = "@syntax.com";

    @Override
    @Transactional
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        log.info("Processing employee creation for: {} {}", request.getFirstName(), request.getLastName());

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with id: " + request.getDepartmentId()));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Position Not Found with id: " + request.getPositionId()));

        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepo.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager Not Found with id: " + request.getManagerId()));
        }

        if (employeeRepo.existsByPersonalEmail(request.getPersonalEmail())) {
            throw new DuplicateResourceException("Personal Email Already Exists");
        }

        String workEmail = generateWorkEmail(request.getFirstName(),request.getLastName());
        String employeeCode = generateEmployeeCode(department.getCode());

        Employee newEmployee = new Employee();

        newEmployee.setEmployeeCode(employeeCode);
        newEmployee.setFirstName(request.getFirstName());
        newEmployee.setLastName(request.getLastName());
        newEmployee.setEmail(workEmail);
        newEmployee.setPersonalEmail(request.getPersonalEmail());
        newEmployee.setDateOfBirth(request.getDateOfBirth());
        newEmployee.setHireDate(request.getHireDate());
        newEmployee.setPhoneNumber(request.getPhoneNumber());
        newEmployee.setDepartment(department);
        newEmployee.setPosition(position);
        newEmployee.setManager(manager);


        employeeRepo.save(newEmployee);

        return CreateEmployeeResponse.builder()
                .employeeCode(employeeCode)
                .fullName(request.getFirstName() + " " + request.getLastName())
                .email(workEmail)
                .build();
    }



    public String generateEmployeeCode(String departmentCode) {

        // 1. Get the current two-digit year (e.g., 2025 -> "25")
        String currentTwoDigitYear = String.valueOf(Year.now().getValue() % 100);
        String yearPrefix = String.format(Locale.ROOT, "%02d", Integer.parseInt(currentTwoDigitYear));

        // Define the common prefix to search for, e.g., "DEV-25-"
        String searchPrefix = departmentCode.toUpperCase(Locale.ROOT) + "-" + yearPrefix + "-";

        // 2. Find the highest existing code for this prefix
        // The repository method must now return the max code for the specific year.
        String maxCode = employeeRepo.findMaxEmployeeCodeByYearAndDepartment(searchPrefix);

        int nextNumber;

        if (maxCode == null) {
            // 3. If no employees exist with this prefix, start at 1
            nextNumber = 1;
        } else {
            // 4. Extract the number part from the max code (e.g., "DEV-25-042" -> 42)
            String numberPart = maxCode.substring(maxCode.lastIndexOf('-') + 1);
            int currentNumber = Integer.parseInt(numberPart);

            // 5. Increment the number
            nextNumber = currentNumber + 1;
        }

        // 6. Format the sequential number with leading zeros (e.g., 1 -> "001")
        String sequentialId = String.format(Locale.ROOT, "%03d", nextNumber);

        // 7. Combine all parts: DEPT_CODE-YY-XXX
        return searchPrefix + sequentialId;
    }

    private String generateWorkEmail(String firstName, String lastName) {
        String base = firstName.toLowerCase().charAt(0) + lastName.toLowerCase();
        String email = base + COMPANY_EMAIL_DOMAIN;
        if (employeeRepo.existsByEmail(email)) {
            String email2 = firstName.toLowerCase() + "." + lastName.toLowerCase()+ COMPANY_EMAIL_DOMAIN;
            if (!employeeRepo.existsByEmail(email2)) {
                return email2;
            } else {
                int counter = 1;
                while (true) {
                    String finalEmail = base + counter + COMPANY_EMAIL_DOMAIN;
                    if (!employeeRepo.existsByEmail(finalEmail)) {
                        return finalEmail;
                    }
                    counter++;
                }
            }
        }
        return email;
    }



}

