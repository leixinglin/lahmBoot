package com.lhh.lahm.schedule;

import org.apache.logging.log4j.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;


@Component
public class TestTask {

    protected Logger log = LoggerFactory.getLogger(TestTask.class);
    @Scheduled(cron = "0 0/5 * * * ?")
    public void work() {
        log.info("test Task1");
        // task execution logic
    }
    @Scheduled(cron = "0 0/15 * * * ?")
    public void work2() {
        log.info("test Task2");
        // task execution logic
    }

    @Scheduled(cron = "0 0/25 * * * ?")
    public void work3() {
        log.info("test Task3");
        // task execution logic
    }

}
