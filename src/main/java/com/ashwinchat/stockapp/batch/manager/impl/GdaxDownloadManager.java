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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ashwinchat.stockapp.batch.constant.BatchConstants;
import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.dao.impl.ISystemConfigDao;
import com.ashwinchat.stockapp.model.pk.StockPrimaryKey;
import com.ashwinchat.stockapp.model.view.StockHistoryView;
import com.ashwinchat.stockapp.model.view.StockScheduleView;
import com.ashwinchat.stockapp.model.view.StockStagingView;

@Component("gdaxDownloadManager")
@Transactional
public class GdaxDownloadManager implements IDownloadManager {

    private String endpointUri;
    private static final String DOWNLOAD_ERROR = "Error in downloading. (name: %s, type: %s, start: %s, end: %s";
    private static final DateTimeFormatter ISO_8601_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Logger log = Logger.getLogger(GdaxDownloadManager.class.getName());

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockScheduleView> scheduleDao;

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockHistoryView> historyDao;

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockStagingView> stagingDao;

    @Autowired
    private ISystemConfigDao systemConfigDao;

    @Override
    public void execute(String stockName) throws Exception {
        if (StringUtils.isBlank(this.endpointUri)) {
            this.endpointUri = this.systemConfigDao.findValue(BatchConstants.GDAX_SYS_CD, BatchConstants.ENDPOINT_URI);
        }
        // 1. Query last date
        StockScheduleView schedule = this.queryLastDate(stockName);
        if (Objects.isNull(schedule)) {
            return;
        }

        // 2. Download from last date to today in batches of 200 days and persist.
        // Can't loop else we get a 429 error
        LocalDateTime startDate = schedule.getNextStartDate();
        if (Objects.isNull(startDate) || this.greaterThan(startDate, this.getStartOfToday())) {
            return;
        }
        LocalDateTime endDate = this.downloadAndPersist(startDate, stockName);

        // 3. update last date.
        schedule.setNextStartDate(endDate.plusDays(1));
        this.scheduleDao.save(schedule);
    }

    private LocalDateTime downloadAndPersist(LocalDateTime start, String stockName) throws Exception {
        LocalDateTime startDate = start;
        LocalDateTime endDate = this.add200DaysOrToday(startDate);

        boolean shouldDownload = this.greaterThan(this.getStartOfToday(), startDate);
        if (shouldDownload) {
            List<StockHistoryView> views = this.download(BatchConstants.STOCK_TYPE_CRYPTO, stockName,
                    this.convertToIso8601String(startDate), this.convertToIso8601String(endDate));

            this.historyDao.saveAll(views);
            List<StockStagingView> stagingViews = views.stream().map(this::mapToStagingView)
                    .collect(Collectors.toList());

            this.stagingDao.saveAll(stagingViews);
        }

        return endDate;
    }

    private StockStagingView mapToStagingView(StockHistoryView histView) {
        StockStagingView stagingView = new StockStagingView();
        stagingView.setProcFlag(BatchConstants.PROC_FLAG_NEW);
        stagingView.setRetryCount(0);
        StockPrimaryKey pk = new StockPrimaryKey();
        pk.setEpochTime(histView.getPk().getEpochTime());
        pk.setStockName(histView.getPk().getStockName());
        pk.setStockTyp(BatchConstants.STOCK_TYPE_CRYPTO);
        stagingView.setPk(pk);

        return stagingView;
    }

    private String convertToIso8601String(LocalDateTime date) {
        return date.format(ISO_8601_FORMAT);
    }

    private LocalDateTime add200DaysOrToday(LocalDateTime date) {
        return this.greaterThan(date.plusDays(200), this.getStartOfToday()) ? this.getStartOfToday()
                : date.plusDays(200);
    }

    private LocalDateTime getStartOfToday() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    }

    private boolean greaterThan(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) > 0;
    }

    private StockScheduleView queryLastDate(String stockName) {
        Query<StockScheduleView> query = this.scheduleDao.createQuery(
                "from StockScheduleView where pk.stockName = :stockName and pk.stockTyp = :stockType and pk.jobTyp = :jobTyp");
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

    private String downloadFromGdax(String stockName, String start, String end) throws IOException {
        String downloadUrl = String.format(endpointUri, stockName, start, end);
        try (InputStream in = new URL(downloadUrl).openStream()) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        }
    }

}
