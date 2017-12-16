package com.ashwinchat.stockapp.batch.manager.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ashwinchat.stockapp.batch.manager.IAnalysisManager;

@Component("stochasticRsiAnalysisManager")
@Transactional
public class StochasticRsiAnalysisManager implements IAnalysisManager {
    private long n;
    private long smaCount;

    @Override
    public void analyse(String stockName, String stockTyp, long stockInterval) {
        // query for n from sys cfg
        // Query for smaCount for %d from sys cfg

        // PROCESSING for %K
        // select MAX(epochTime) from StochasticRsiView where pk.stockName =
        // stockName and pk.stockTyp = stockTyp and pk.stockInterval = stockInterval

        // Loop from queried epochTime + interval to most recent epochTime.
        // -- Query history table based on stockName, stockType and epochTime(s)
        // -- -- (depending on value n)
        // -- process %k based on this formula:
        // -- 100 * ((close(current) - LowestLow(n))/(HighestHigh(n) - LowestLow(n)))
        // -- persist into table
        // PROCESSING for %K end

        // PROCESSING for %D
        // select MAX(epochTime) from StochasticRsiView where pk.stockName =
        // stockName and pk.stockTyp = stockTyp and pk.stockInterval = stockInterval and
        // d is not null

        // Loop from queried epochTime + interval to most recent epochTime.
        // -- Query history table based on stockName, stockType and epochTime(s)
        // -- -- (depending on value n)
        // -- process %d based on this formula:
        // -- -- Query for the past few records based on smaCount (selecting %k is
        // -- -- enough).
        // -- -- ignore persisting %D if not enough records. Above hql should take care
        // -- -- of things.
        // -- -- if enough records, calc sma.
        // -- persist into table
    }

}
