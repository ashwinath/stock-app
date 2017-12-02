package com.ashwinchat.stockapp.model.dao;

import java.util.List;

import com.ashwinchat.stockapp.model.view.StockHistoryView;

public interface IStockHistoryDao extends IDao<StockHistoryView> {
    List<StockHistoryView> findStocksWithDateRange(String stockTyp, String stockName, long startEpochTime,
            long endEpochTime);
}
