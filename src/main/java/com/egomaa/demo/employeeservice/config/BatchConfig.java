package com.egomaa.demo.employeeservice.config;

import com.egomaa.demo.employeeservice.entity.Employee;
import com.egomaa.demo.employeeservice.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
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
    @StepScope
    public ItemProcessor<Employee, Employee> employeeProcessor(
            @Value("#{jobParameters['amount']}") Double amount) {
        return employee -> {
            log.debug("Processing employee ID: {} - Old Salary: {} - Increase: {}",
                    employee.getId(), employee.getSalary(), amount);
            employee.setSalary(employee.getSalary() + amount);
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
    public Step salaryIncreaseStep(ItemProcessor<Employee, Employee> employeeProcessor) {
        return new StepBuilder("salaryIncreaseStep", jobRepository)
                .<Employee, Employee>chunk(3, platformTransactionManager)
                .reader(employeeReader())
                .processor(employeeProcessor)
                .writer(employeeWriter())
                .build();
    }

    @Bean
    public Step summaryReportStep() {
        return new StepBuilder("summaryReportStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // This runs after all salaries are increased
                    long totalEmployees = employeeRepo.count();
                    Double amount = (Double) chunkContext.getStepContext()
                            .getJobParameters().get("amount");

                    log.info("=".repeat(50));
                    log.info("Salary Increase Job Completed Successfully");
                    log.info("Total employees updated: {}", totalEmployees);
                    log.info("Amount increased per employee: {}", amount);
                    log.info("=".repeat(50));

                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step auditLogStep() {
        return new StepBuilder("auditLogStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // This runs after the summary report
                    Long jobExecutionId = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecutionId();
                    String jobName = chunkContext.getStepContext()
                            .getJobName();

                    log.warn("AUDIT LOG: Job '{}' with execution ID {} completed at {}",
                            jobName,
                            jobExecutionId,
                            java.time.LocalDateTime.now());
                    log.warn("AUDIT LOG: All steps executed successfully");

                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Job salaryIncreaseJob(Step salaryIncreaseStep, Step summaryReportStep, Step auditLogStep) {
        return new JobBuilder("salaryIncreaseJob", jobRepository)
                .start(salaryIncreaseStep) // Step 1: Process and update employee salaries
                .next(summaryReportStep) // Step 2: Generate summary report
                .next(auditLogStep) // Step 3: Log audit information
                .build();
    }
}
