package com.ashwinchat.stockapp.model.view;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ashwinchat.stockapp.model.pk.StockPrimaryKey;

@Entity
@Table(name = "tb_stock_staging")
public class StockStagingView extends DatabaseView {
    private StockPrimaryKey pk;
    private String procFlag;
    private LocalDateTime procOn;
    private String errorMessage;
    private int retryCount;

    @EmbeddedId
    public StockPrimaryKey getPk() {
        return this.pk;
    }

    public void setPk(StockPrimaryKey pk) {
        this.pk = pk;
    }

    @Column(name = "proc_flg")
    public String getProcFlag() {
        return this.procFlag;
    }

    public void setProcFlag(String procFlag) {
        this.procFlag = procFlag;
    }

    @Column(name = "proc_on")
    public LocalDateTime getProcOn() {
        return this.procOn;
    }

    public void setProcOn(LocalDateTime procOn) {
        this.procOn = procOn;
    }

    @Column(name = "err_msg")
    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Column(name = "retry_cnt")
    public int getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

}
