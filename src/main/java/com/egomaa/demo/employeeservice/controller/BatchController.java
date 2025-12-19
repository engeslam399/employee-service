package com.egomaa.demo.employeeservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job salaryIncreaseJob;

    @PostMapping("/increase-salary")
    public ResponseEntity<String> runSalaryIncreaseJob(@RequestParam Double amount) {
        try {
            log.info("Starting salary increase job with amount: {}", amount);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .addDouble("amount", amount)
                    .toJobParameters();

            jobLauncher.run(salaryIncreaseJob, jobParameters);
            return ResponseEntity.ok("Batch job started successfully. Increasing all employee salaries by " + amount);
        } catch (Exception e) {
            log.error("Error starting batch job", e);
            return ResponseEntity.internalServerError().body("Error starting batch job: " + e.getMessage());
        }
    }
}
