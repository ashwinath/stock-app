package com.ashwinchat.stockapp.model.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_system_config")
public class SystemConfigView extends DatabaseView {
    private int id;
    private String sysCd;
    private String key;
    private String value;

    @Id
    @Column(name = "id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "sys_cd")
    public String getSysCd() {
        return this.sysCd;
    }

    public void setSysCd(String sysCd) {
        this.sysCd = sysCd;
    }

    @Column(name = "key")
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
