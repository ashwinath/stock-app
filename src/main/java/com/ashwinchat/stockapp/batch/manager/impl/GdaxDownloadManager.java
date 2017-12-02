package com.ashwinchat.stockapp.batch.manager.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ashwinchat.stockapp.batch.constant.BatchConstants;
import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.dao.ISystemConfigDao;
import com.ashwinchat.stockapp.model.pk.StockPrimaryKey;
import com.ashwinchat.stockapp.model.view.StockHistoryView;
import com.ashwinchat.stockapp.model.view.StockScheduleView;

@Component("gdaxDownloadManager")
@Transactional
public class GdaxDownloadManager implements IDownloadManager {

    private static final int MAX_NUMBER_OF_RECORDS = 200;
    private String endpointUri;
    private static final String DOWNLOAD_ERROR = "Error in downloading. (name: %s, type: %s, start: %s, end: %s";
    private Logger log = LoggerFactory.getLogger(GdaxDownloadManager.class.getName());

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockScheduleView> scheduleDao;

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockHistoryView> historyDao;

    @Autowired
    private ISystemConfigDao systemConfigDao;

    @Override
    public void execute(String stockName) throws Exception {
        long granularity = this.getGranularity();
        log.info(String
            .format("Starting download of stocks from GDAX for type = %s, name = %s", BatchConstants.STOCK_TYPE_CRYPTO, stockName));
        if (StringUtils.isBlank(this.endpointUri)) {
            this.endpointUri = this.systemConfigDao.findValue(BatchConstants.GDAX_SYS_CD, BatchConstants.ENDPOINT_URI)
                    + granularity;
        }

        // 1. Query last date
        StockScheduleView schedule = this.queryLastDate(stockName);
        if (Objects.isNull(schedule)) {
            return;
        }

        // 2. Download from last date to today in batches of 200 days and persist.
        // Can't loop else we get a 429 error
        LocalDateTime startDate = schedule.getNextStartDate();
        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(startDate) || this.greaterThan(startDate, now)) {
            return;
        }
        LocalDateTime endDate = this.downloadAndPersist(now, startDate, stockName, granularity);

        // 3. update last date. Singapore Time
        schedule.setNextStartDate(this.truncateToNearestGranularity(endDate, granularity).plusSeconds(granularity));
        this.scheduleDao.update(schedule);
        log.info(String
            .format("Finished download of stocks from GDAX for type = %s, name = %s", BatchConstants.STOCK_TYPE_CRYPTO, stockName));
    }

    private LocalDateTime truncateToNearestGranularity(LocalDateTime endDate, long granularity) {
        long epochTime = endDate.atZone(ZoneId.systemDefault()).toEpochSecond();
        long roundedOff = epochTime - epochTime % granularity;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(roundedOff), ZoneId.systemDefault());
    }

    /**
     * Download based on GMT time. Not SG time. Returns Singapore time;
     * 
     * @param now
     * @param start
     * @param stockName
     * @param granularity
     * @return
     * @throws Exception
     */
    private LocalDateTime downloadAndPersist(LocalDateTime now, LocalDateTime start, String stockName, long granularity)
            throws Exception {
        LocalDateTime nowGmt = now.minusHours(8);
        LocalDateTime startDate = start.minusHours(8);
        LocalDateTime endDate = this.getEndDate(nowGmt, startDate, granularity);

        boolean shouldDownload = this.greaterThan(nowGmt, startDate);
        if (shouldDownload) {
            List<StockHistoryView> views = this.download(BatchConstants.STOCK_TYPE_CRYPTO, stockName, this
                .convertToIso8601String(startDate), this.convertToIso8601String(endDate));

            this.historyDao.saveAll(views);
        }

        return endDate.plusHours(8);
    }

    private long getGranularity() {
        try {
            String granularityString = this.systemConfigDao
                .findValue(BatchConstants.GDAX_SYS_CD, BatchConstants.GRANULARITY_KEY);
            return Long.parseLong(granularityString);
        } catch (NumberFormatException e) {
            log.warn(String
                .format("Please put an integer under SYS_CD = %s, KEY = %s. Defaulting to 200 records in one day.", BatchConstants.GDAX_SYS_CD, BatchConstants.GRANULARITY_KEY));
            return 432;
        }
    }

    private String convertToIso8601String(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private LocalDateTime getEndDate(LocalDateTime now, LocalDateTime date, long granularityInSeconds) {
        LocalDateTime nextDate = date.plusSeconds(granularityInSeconds * MAX_NUMBER_OF_RECORDS);
        return this.greaterThan(nextDate, now) ? now : nextDate;
    }

    private boolean greaterThan(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) > 0;
    }

    private StockScheduleView queryLastDate(String stockName) {
        Query<StockScheduleView> query = this.scheduleDao
            .createQuery("from StockScheduleView where pk.stockName = :stockName and pk.stockTyp = :stockType and pk.jobTyp = :jobTyp");
        query.setParameter("stockName", stockName);
        query.setParameter("stockType", BatchConstants.STOCK_TYPE_CRYPTO);
        query.setParameter("jobTyp", BatchConstants.JOB_TYPE_DOWNLOAD);
        List<StockScheduleView> views = query.list();
        if (CollectionUtils.isNotEmpty(views)) {
            return views.get(0);
        }
        return null;
    }

    private List<StockHistoryView> download(String stockType, String stockName, String start, String end)
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
        view.setTime(LocalDateTime
            .ofInstant(Instant.ofEpochSecond((int) eachDayRecords.get(0)), ZoneId.systemDefault()));
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

    private String downloadFromGdax(String stockName, String start, String end) throws IOException {
        String downloadUrl = String.format(endpointUri, stockName, start, end);
        if (log.isDebugEnabled()) {
            log.debug("Endpoint Url: " + downloadUrl);
        }

        try (InputStream in = new URL(downloadUrl).openStream()) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        }
    }

}
