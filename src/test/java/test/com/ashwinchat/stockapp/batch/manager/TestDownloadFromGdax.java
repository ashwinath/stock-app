package test.com.ashwinchat.stockapp.batch.manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ashwinchat.stockapp.batch.constant.BatchConstants;
import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.config.BatchConfig;
import com.ashwinchat.stockapp.config.HibernateConfig;
import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.dao.ISystemConfigDao;
import com.ashwinchat.stockapp.model.pk.StockSchedulePrimaryKey;
import com.ashwinchat.stockapp.model.view.StockHistoryView;
import com.ashwinchat.stockapp.model.view.StockScheduleView;
import com.ashwinchat.stockapp.model.view.SystemConfigView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BatchConfig.class, HibernateConfig.class })
@Transactional
public class TestDownloadFromGdax {

    private static final String COIN_TYPE = "LTC-BTC";
    private static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
    private StockScheduleView scheduleView;
    private static int NUMBER_OF_SECONDS_FOR_200_RECORDS_IN_ONE_DAY = 432;

    @Autowired
    @Qualifier("gdaxDownloadManager")
    private IDownloadManager gdaxDownloadManager;

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockHistoryView> historyDao;

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockScheduleView> scheduleDao;

    @Autowired
    private ISystemConfigDao systemConfigDao;

    @Before
    public void init() {
        this.insertConfig();
        this.setGranularity();
    }

    private void setGranularity() {
        Query<SystemConfigView> query = this.systemConfigDao
            .createQuery("from SystemConfigView where sysCd = :sysCd and key = :key");
        query.setParameter("sysCd", BatchConstants.GDAX_SYS_CD);
        query.setParameter("key", BatchConstants.GRANULARITY_KEY);
        SystemConfigView sysConfig = query.uniqueResult();
        if (Objects.nonNull(sysConfig)) {
            sysConfig.setValue("" + NUMBER_OF_SECONDS_FOR_200_RECORDS_IN_ONE_DAY);
            this.systemConfigDao.update(sysConfig);
        } else {
            sysConfig = new SystemConfigView();
            sysConfig.setId(10000000);
            sysConfig.setSysCd(BatchConstants.GDAX_SYS_CD);
            sysConfig.setKey(BatchConstants.GRANULARITY_KEY);
            sysConfig.setValue("" + NUMBER_OF_SECONDS_FOR_200_RECORDS_IN_ONE_DAY);
            this.systemConfigDao.save(sysConfig);
        }
    }

    @After
    public void cleanup() {
        if (Objects.nonNull(this.scheduleView)) {
            this.scheduleDao.delete(this.scheduleView);
        }
    }

    @Test
    public void testNominal() throws Exception {
        this.gdaxDownloadManager.execute(COIN_TYPE);
        this.assertHistTable();
    }

    private void insertConfig() {
        this.scheduleView = new StockScheduleView();
        this.scheduleView.setNextStartDate(YESTERDAY);
        StockSchedulePrimaryKey pk = new StockSchedulePrimaryKey();
        pk.setJobTyp(BatchConstants.JOB_TYPE_DOWNLOAD);
        pk.setStockTyp(BatchConstants.STOCK_TYPE_CRYPTO);
        pk.setStockName(COIN_TYPE);
        scheduleView.setPk(pk);
        this.scheduleDao.save(this.scheduleView);
    }

    private void assertHistTable() {
        Query<StockHistoryView> query = this.historyDao
            .createQuery("from StockHistoryView where pk.stockTyp = :stockTyp and pk.stockName = :stockName");
        query.setParameter("stockTyp", BatchConstants.STOCK_TYPE_CRYPTO);
        query.setParameter("stockName", COIN_TYPE);
        List<StockHistoryView> histViews = query.list();
        Assert.assertTrue(CollectionUtils.isNotEmpty(histViews));
        for (StockHistoryView view : histViews) {
            Assert.assertNotNull(view.getClose());
            Assert.assertNotNull(view.getHigh());
            Assert.assertNotNull(view.getOpen());
            Assert.assertNotNull(view.getLow());
            Assert.assertNotNull(view.getVolume());
            Assert.assertNotNull(view.getTime());
            Assert.assertNotNull(view.getPk());
            Assert.assertNotNull(view.getPk().getStockName());
            Assert.assertNotNull(view.getPk().getStockTyp());
            Assert.assertNotNull(view.getPk().getEpochTime());
        }
    }
}
