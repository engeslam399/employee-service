package com.egomaa.demo.employeeservice.constants;

public interface AppConstants {

    // Paths
    String BASE_API = "/api";
    String EMPLOYEES_API = BASE_API + "/employees";
    String DEPARTMENTS_API = BASE_API + "/departments";

    // Success Messages
    String EMPLOYEE_CREATED = "Employee created successfully";
    String EMPLOYEE_RETRIEVED = "Employee retrieved successfully";
    String EMPLOYEES_RETRIEVED = "All employees retrieved successfully";
    String EMPLOYEE_UPDATED = "Employee updated successfully";
    String EMPLOYEE_DELETED = "Employee deleted successfully";

    String DEPARTMENT_CREATED = "Department created successfully";
    String DEPARTMENT_RETRIEVED = "Department retrieved successfully";
    String DEPARTMENTS_RETRIEVED = "All departments retrieved successfully";
    String DEPARTMENT_UPDATED = "Department updated successfully";
    String DEPARTMENT_DELETED = "Department deleted successfully";

    // Error Messages
    String EMPLOYEE_NOT_FOUND = "Employee not found";
    String EMPLOYEE_DUPLICATE_EMAIL = "Email already exists";
    String DEPARTMENT_NOT_FOUND = "Department not found";
    String DEPARTMENT_DUPLICATE_NAME = "Department name already exists";

}
