package com.ashwinchat.stockapp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.ashwinchat.stockapp.quartz.jobfactory.AutowiringSpringBeanJobFactory;

@Configuration
@ComponentScan(basePackages = { "com.ashwinchat.stockapp.batch" })
public class BatchConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Bean("quartzScheduler")
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
        quartzScheduler.setTransactionManager(this.transactionManager);

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(this.applicationContext);
        quartzScheduler.setJobFactory(jobFactory);
        quartzScheduler.setConfigLocation(new ClassPathResource("/quartz.properties"));
        quartzScheduler.setSchedulerName("StockScheduler");
        quartzScheduler.setDataSource(dataSource);
        quartzScheduler.setWaitForJobsToCompleteOnShutdown(true);

        return quartzScheduler;
    }

}
