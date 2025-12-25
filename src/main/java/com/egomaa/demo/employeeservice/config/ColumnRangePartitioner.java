package com.egomaa.demo.employeeservice.config;

import com.egomaa.demo.employeeservice.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ColumnRangePartitioner implements Partitioner {

    private final EmployeeRepo employeeRepo;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long min = employeeRepo.findMinId();
        long max = employeeRepo.findMaxId();
        long targetSize = (max - min) / gridSize + 1;

        Map<String, ExecutionContext> result = new HashMap<>();
        long number = 0;
        long start = min;
        long end = start + targetSize - 1;

        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= max) {
                end = max;
            }
            value.putLong("minId", start);
            value.putLong("maxId", end);
            start += targetSize;
            end += targetSize;
            number++;
        }
        return result;
    }
}
