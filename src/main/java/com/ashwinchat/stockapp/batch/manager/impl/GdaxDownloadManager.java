package com.ashwinchat.stockapp.batch.manager.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.json.JSONArray;

import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.model.pk.StockPrimaryKey;
import com.ashwinchat.stockapp.model.view.StockHistoryView;

public class GdaxDownloadManager implements IDownloadManager {

    private static final String URL_FORMAT = "https://api.gdax.com/products/%s/candles?start=%s&end=%s&granularity=600";
    private static final String DOWNLOAD_ERROR = "Error in downloading. (name: %s, type: %s, start: %s, end: %s";
    private Logger log = Logger.getLogger(GdaxDownloadManager.class.getName());

    @Override
    public List<StockHistoryView> download(String stockType, String stockName, String start, String end)
            throws Exception {
        try {
            String jsonString = this.downloadFromGdax(stockName, start, end);
            List<StockHistoryView> results = this.unmarshal(jsonString);
            results.forEach(result -> result.getPk().setStockName(stockName));
            results.forEach(result -> result.getPk().setStockTyp(stockType));
            return results;
        } catch (Exception e) {
            log.error(String.format(DOWNLOAD_ERROR, stockName, stockType, start, end));
            throw e;
        }
    }

    /*
     * Sample: [ [time, low, high, open, close, volume], [...], ]
     */
    private List<StockHistoryView> unmarshal(String jsonString) {
        JSONArray allDays = new JSONArray(jsonString);
        List<StockHistoryView> result = new ArrayList<>();
        for (int i = 0; i < allDays.length(); ++i) {
            JSONArray eachDayRecords = allDays.getJSONArray(i);
            StockHistoryView view = this.mapToView(eachDayRecords);
            result.add(view);
        }
        return result;
    }

    private StockHistoryView mapToView(JSONArray eachDayRecords) {
        StockHistoryView view = new StockHistoryView();
        view.setTime(
                LocalDateTime.ofInstant(Instant.ofEpochSecond((int) eachDayRecords.get(0)), ZoneId.systemDefault()));
        view.setLow(new BigDecimal(eachDayRecords.get(1).toString()));
        view.setHigh(new BigDecimal(eachDayRecords.get(2).toString()));
        view.setOpen(new BigDecimal(eachDayRecords.get(3).toString()));
        view.setClose(new BigDecimal(eachDayRecords.get(4).toString()));
        view.setVolume(new BigDecimal(eachDayRecords.get(5).toString()));

        StockPrimaryKey pk = new StockPrimaryKey();
        pk.setEpochTime((int) eachDayRecords.get(0));
        view.setPk(pk);

        return view;

    }

    private String downloadFromGdax(String stockName, String start, String end)
            throws MalformedURLException, IOException {
        String downloadUrl = String.format(URL_FORMAT, stockName, start, end);
        try (InputStream in = new URL(downloadUrl).openStream()) {
            String jsonResult = IOUtils.toString(in, StandardCharsets.UTF_8);
            return jsonResult;
        }
    }

}
