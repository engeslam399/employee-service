package com.egomaa.demo.employeeservice.repo;

import com.egomaa.demo.employeeservice.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepo extends JpaRepository<Configuration, Long> {
    Configuration findByKey(String key);
}
