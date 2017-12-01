package com.ashwinchat.stockapp.config;

import java.util.logging.Logger;

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

    private Logger log = Logger.getLogger(BatchConfig.class.getName());

    @Bean("quartzScheduler")
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
        quartzScheduler.setTransactionManager(this.transactionManager);

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(this.applicationContext);
        quartzScheduler.setJobFactory(jobFactory);
        quartzScheduler.setConfigLocation(new ClassPathResource("/quartz.properties"));
        quartzScheduler.setSchedulerName("StockScheduler");

        return quartzScheduler;
    }

}
