package com.ashwinchat.stockapp.model.dao;

import java.util.List;

import org.hibernate.query.Query;

import com.ashwinchat.stockapp.model.view.DatabaseView;

public interface IDao<V extends DatabaseView> {
    void save(V view);

    void saveAll(List<V> views);

    void update(V view);

    void updateAll(List<V> views);

    void delete(V view);

    void deleteAll(List<V> views);

    Query<V> createQuery(String hql);
}
