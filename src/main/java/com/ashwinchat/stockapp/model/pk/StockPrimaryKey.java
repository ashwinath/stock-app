package com.ashwinchat.stockapp.model.pk;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StockPrimaryKey implements Serializable {

    private static final long serialVersionUID = 787277336455770451L;

    private String stockTyp;
    private String stockName;
    private long epochTime;

    @Column(name = "stock_typ")
    public String getStockTyp() {
        return this.stockTyp;
    }

    public void setStockTyp(String stockTyp) {
        this.stockTyp = stockTyp;
    }

    @Column(name = "stock_name")
    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Column(name = "epoch_time")
    public long getEpochTime() {
        return this.epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.nonNull(obj) && obj instanceof StockPrimaryKey) {
            StockPrimaryKey otherObj = (StockPrimaryKey) obj;

            return otherObj.getEpochTime() == this.getEpochTime()
                    && Objects.equals(otherObj.getStockName(), this.getStockName())
                    && Objects.equals(otherObj.getStockTyp(), this.getStockTyp());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.epochTime, this.stockName, this.stockTyp);
    }

}
