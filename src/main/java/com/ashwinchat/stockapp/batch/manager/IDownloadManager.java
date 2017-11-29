package com.ashwinchat.stockapp.batch.manager;

public interface IDownloadManager {
    void execute(String stockName) throws Exception;
}
