package com.ashwinchat.stockapp.model.view;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ashwinchat.stockapp.model.pk.StockSchedulePrimaryKey;

@Entity
@Table(name = "tb_stock_schedule")
public class StockScheduleView extends DatabaseView {

    private StockSchedulePrimaryKey pk;
    private LocalDateTime nextStartDate;

    @EmbeddedId
    public StockSchedulePrimaryKey getPk() {
        return this.pk;
    }

    public void setPk(StockSchedulePrimaryKey pk) {
        this.pk = pk;
    }

    @Column(name = "next_start_dt")
    public LocalDateTime getNextStartDate() {
        return this.nextStartDate;
    }

    public void setNextStartDate(LocalDateTime nextStartDate) {
        this.nextStartDate = nextStartDate;
    }

}
