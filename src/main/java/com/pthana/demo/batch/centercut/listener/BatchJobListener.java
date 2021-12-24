package com.pthana.demo.batch.centercut.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class BatchJobListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
       log.info("Batch Start!!!");

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
       log.info("Batch Finish!!!");
    }
}