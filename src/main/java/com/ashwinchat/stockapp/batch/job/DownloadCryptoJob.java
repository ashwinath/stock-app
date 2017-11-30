package com.ashwinchat.stockapp.batch.job;

import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ashwinchat.stockapp.batch.constant.BatchConstants;
import com.ashwinchat.stockapp.batch.manager.IDownloadManager;

public class DownloadCryptoJob implements Job {

    @Autowired
    @Qualifier("gdaxDownloadManager")
    private IDownloadManager gdaxDownloadManager;

    private Logger log = Logger.getLogger(JobFactory.class.getName());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String stockName = context.getJobDetail().getJobDataMap().getString(BatchConstants.STOCK_NAME_KEY);
        try {
            this.gdaxDownloadManager.execute(stockName);
        } catch (Exception e) {
            log.severe(ExceptionUtils.getStackTrace(e));
            throw new JobExecutionException(e);
        }
    }

}
