package com.ashwinchat.stockapp.batch.manager;

public interface IAnalysisManager {
    void analyse(String stockName, String stockTyp, long stockInterval);
}
