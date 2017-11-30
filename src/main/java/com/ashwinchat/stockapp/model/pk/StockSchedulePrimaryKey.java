package com.ashwinchat.stockapp.model.pk;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StockSchedulePrimaryKey implements Serializable {

    private static final long serialVersionUID = -4849418757568549933L;

    private String stockTyp;
    private String stockName;
    private String jobTyp;

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

    @Column(name = "job_typ")
    public String getJobTyp() {
        return this.jobTyp;
    }

    public void setJobTyp(String jobTyp) {
        this.jobTyp = jobTyp;
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.nonNull(obj) && obj instanceof StockSchedulePrimaryKey) {
            StockSchedulePrimaryKey otherObj = (StockSchedulePrimaryKey) obj;

            return Objects.equals(otherObj.getJobTyp(), this.getJobTyp())
                    && Objects.equals(otherObj.getStockName(), this.getStockName())
                    && Objects.equals(otherObj.getStockTyp(), this.getStockTyp());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jobTyp, this.stockName, this.stockTyp);
    }
}
