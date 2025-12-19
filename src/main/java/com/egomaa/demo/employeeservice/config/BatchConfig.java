package com.egomaa.demo.employeeservice.config;

import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final EmployeeRepo employeeRepo;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public RepositoryItemReader<Employee> employeeReader() {
        return new RepositoryItemReaderBuilder<Employee>()
                .name("employeeReader")
                .repository(employeeRepo)
                .methodName("findAll")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .pageSize(10)
                .saveState(false)
                .build();
    }

    @Bean
    public ItemProcessor<Employee, Employee> employeeProcessor() {
        return employee -> {
            employee.setSalary(employee.getSalary() + 1000);
            return employee;
        };
    }

    @Bean
    public RepositoryItemWriter<Employee> employeeWriter() {
        return new RepositoryItemWriterBuilder<Employee>()
                .repository(employeeRepo)
                .methodName("save")
                .build();
    }

    @Bean
    public Step salaryIncreaseStep() {
        return new StepBuilder("salaryIncreaseStep", jobRepository)
                .<Employee, Employee>chunk(6, platformTransactionManager)
                .reader(employeeReader())
                .processor(employeeProcessor())
                .writer(employeeWriter())
                .build();
    }

    @Bean
    public Job salaryIncreaseJob() {
        return new JobBuilder("salaryIncreaseJob", jobRepository)
                .start(salaryIncreaseStep())
                .build();
    }
}
