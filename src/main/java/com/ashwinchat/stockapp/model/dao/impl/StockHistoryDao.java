package com.ashwinchat.stockapp.model.dao.impl;

import java.util.List;

import org.hibernate.query.Query;

import com.ashwinchat.stockapp.model.dao.IStockHistoryDao;
import com.ashwinchat.stockapp.model.view.StockHistoryView;

public class StockHistoryDao extends Dao<StockHistoryView> implements IStockHistoryDao {

    @Override
    public List<StockHistoryView> findStocksWithDateRange(String stockTyp, String stockName, long startEpochTime,
            long endEpochTime) {
        Query<StockHistoryView> query = this
            .createQuery("from StockHistoryView where pk.stockTyp = :stockTyp and (pk.stockName = :stockName and pk.epochTime >= :startEpochTime or pk.endEpochTime <= :endEpochTime)");
        query.setParameter("stockTyp", stockTyp);
        query.setParameter("stockName", stockName);
        query.setParameter("startEpochTime", startEpochTime);
        query.setParameter("endEpochTime", endEpochTime);
        return query.list();
    }

}
