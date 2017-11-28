package com.ashwinchat.stockapp.batch.manager;

import java.util.List;

import com.ashwinchat.stockapp.model.view.StockHistoryView;

public interface IDownloadManager {
    List<StockHistoryView> download(String stockType, String stockName, String start, String end) throws Exception;
}
