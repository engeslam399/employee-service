package com.egomaa.demo.employeeservice.service;

import com.egomaa.demo.employeeservice.entity.Configuration;
import com.egomaa.demo.employeeservice.repo.ConfigurationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepo configurationRepo;


    public Configuration getByKey(String key) {
        return configurationRepo.findByKey(key);
    }
}
