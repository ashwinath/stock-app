package com.ashwinchat.stockapp.model.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_job_config")
public class JobConfigView extends DatabaseView {
    private String jobName;
    private String jobType;
    private String stockName;
    private String service;
    private int interval;
    private String stat;

    @Id
    @Column(name = "job_name")
    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Column(name = "job_type")
    public String getJobType() {
        return this.jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Column(name = "stock_name")
    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Column(name = "service")
    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Column(name = "interval")
    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Column(name = "stat")
    public String getStat() {
        return this.stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
