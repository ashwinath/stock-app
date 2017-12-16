package com.ashwinchat.stockapp.model.view;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ashwinchat.stockapp.model.pk.AnalysePrimaryKey;

@Entity
@Table(name = "tb_stochastic_rsi")
public class StochasticRsiView extends DatabaseView {

    private AnalysePrimaryKey pk;
    private BigDecimal k;
    private BigDecimal d;

    @EmbeddedId
    public AnalysePrimaryKey getPk() {
        return this.pk;
    }

    public void setPk(AnalysePrimaryKey pk) {
        this.pk = pk;
    }

    @Column(name = "k")
    public BigDecimal getK() {
        return this.k;
    }

    public void setK(BigDecimal k) {
        this.k = k;
    }

    @Column(name = "d")
    public BigDecimal getD() {
        return this.d;
    }

    public void setD(BigDecimal d) {
        this.d = d;
    }

}
