package com.ashwinchat.stockapp.batch.job;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.ashwinchat.stockapp.batch.constant.BatchConstants;
import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.view.JobConfigView;

@Component
@Transactional
public class JobFactory {

    @Autowired
    @Qualifier("genericDao")
    private IDao<JobConfigView> jobConfigDao;

    @Autowired
    @Qualifier("quartzScheduler")
    private SchedulerFactoryBean schedulerFactoryBean;

    private Scheduler scheduler;

    @PostConstruct
    public void init() {
        this.scheduler = schedulerFactoryBean.getScheduler();
    }

    public void createAllCryptoJob() throws SchedulerException {
        List<JobConfigView> configs = this.getAllActiveJobs(BatchConstants.GDAX_SYS_CD);
        for (JobConfigView config : configs) {
            this.createDownloadCryptoJob(config);
        }
    }

    private void createDownloadCryptoJob(JobConfigView configView) throws SchedulerException {
        JobDetail job = JobBuilder
            .newJob(DownloadCryptoJob.class)
            .withIdentity(configView.getJobName())
            .usingJobData(BatchConstants.STOCK_NAME_KEY, configView.getStockName())
            .build();

        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity(configView.getJobName())
            .startNow()
            .withSchedule(SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMinutes(configView.getInterval())
                .repeatForever())
            .build();

        this.scheduler.scheduleJob(job, trigger);
    }

    private List<JobConfigView> getAllActiveJobs(String service) {
        Query<JobConfigView> query = this.jobConfigDao
            .createQuery("from JobConfigView where stat = :stat and service = :service");
        query.setParameter("stat", BatchConstants.ACTIVE_STAT);
        query.setParameter("service", service);
        return query.list();
    }
}
