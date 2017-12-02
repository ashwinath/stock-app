package com.ashwinchat.stockapp.model.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.view.DatabaseView;
import com.ashwinchat.stockapp.model.view.embeddable.EmbeddableTimeStamps;

@Repository("genericDao")
public class Dao<V extends DatabaseView> implements IDao<V> {

    private static final int UPDATE_FLAG = 1;
    private static final int INSERT_FLAG = 2;

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public void save(V view) {
        this.setAuditFields(INSERT_FLAG, view);
        this.sessionFactory.getCurrentSession().save(view);
    }

    @Override
    public void saveAll(List<V> views) {
        for (V view : views) {
            this.setAuditFields(INSERT_FLAG, view);
            this.sessionFactory.getCurrentSession().save(view);
        }
    }

    @Override
    public void update(V view) {
        this.setAuditFields(UPDATE_FLAG, view);
        this.sessionFactory.getCurrentSession().update(view);
    }

    @Override
    public void updateAll(List<V> views) {
        for (V view : views) {
            this.setAuditFields(UPDATE_FLAG, view);
            this.sessionFactory.getCurrentSession().update(view);
        }
    }

    @Override
    public void delete(V view) {
        this.sessionFactory.getCurrentSession().remove(view);
    }

    @Override
    public void deleteAll(List<V> views) {
        for (V view : views) {
            this.sessionFactory.getCurrentSession().remove(view);
        }
    }

    private void setAuditFields(int flag, V view) {
        EmbeddableTimeStamps timeStamps;
        if (UPDATE_FLAG == flag) {
            timeStamps = view.getTimeStamps();
            timeStamps.setUpdatedOn(LocalDateTime.now());
        } else {
            timeStamps = new EmbeddableTimeStamps();
            timeStamps.setUpdatedOn(LocalDateTime.now());
            timeStamps.setCreatedOn(LocalDateTime.now());
            view.setTimeStamps(timeStamps);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query<V> createQuery(String hql) {
        return this.sessionFactory.getCurrentSession().createQuery(hql);
    }
}
