package com.ashwinchat.stockapp.model.dao.impl;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.ashwinchat.stockapp.model.dao.ISystemConfigDao;
import com.ashwinchat.stockapp.model.view.SystemConfigView;

@Repository
public class SystemConfigDao extends Dao<SystemConfigView> implements ISystemConfigDao {

    @SuppressWarnings("unchecked")
    @Override
    public String findValue(String sysCd, String key) {
        Query<String> query = this.sessionFactory
            .getCurrentSession()
            .createQuery("select value from SystemConfigView where sysCd = :sysCd and key = :key");
        query.setParameter("sysCd", sysCd);
        query.setParameter("key", key);
        return query.uniqueResult();
    }

}
