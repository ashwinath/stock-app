package com.ashwinchat.stockapp.main;

import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ashwinchat.stockapp.batch.job.JobFactory;
import com.ashwinchat.stockapp.config.BatchConfig;
import com.ashwinchat.stockapp.config.HibernateConfig;

public class Main {
    private static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BatchConfig.class, HibernateConfig.class);
        JobFactory jobFactory = context.getBean(JobFactory.class);
        try {
            jobFactory.createAllCryptoJob();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            log.severe(ExceptionUtils.getStackTrace(e));
        } finally {
            ((ConfigurableApplicationContext) context).close();
        }
    }

}
