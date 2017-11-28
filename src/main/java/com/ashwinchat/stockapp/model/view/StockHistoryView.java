package com.ashwinchat.stockapp.model.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ashwinchat.stockapp.model.pk.StockPrimaryKey;

@Entity
@Table(name = "tb_stock_hist")
public class StockHistoryView extends DatabaseView {

    private StockPrimaryKey pk;
    private LocalDateTime time;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal volume;

    @EmbeddedId
    public StockPrimaryKey getPk() {
        return this.pk;
    }

    public void setPk(StockPrimaryKey pk) {
        this.pk = pk;
    }

    @Column(name = "time")
    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Column(name = "low")
    public BigDecimal getLow() {
        return this.low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    @Column(name = "high")
    public BigDecimal getHigh() {
        return this.high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    @Column(name = "open")
    public BigDecimal getOpen() {
        return this.open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    @Column(name = "close")
    public BigDecimal getClose() {
        return this.close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    @Column(name = "volume")
    public BigDecimal getVolume() {
        return this.volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

}
