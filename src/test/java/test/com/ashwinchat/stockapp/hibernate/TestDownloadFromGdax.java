package test.com.ashwinchat.stockapp.hibernate;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.config.BatchConfig;
import com.ashwinchat.stockapp.model.view.StockHistoryView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BatchConfig.class })
public class TestDownloadFromGdax {

    @Autowired
    @Qualifier("gdaxDownloadManager")
    private IDownloadManager gdaxDownloadManager;

    @Test
    public void testNominal() throws Exception {
        List<StockHistoryView> result = gdaxDownloadManager.download("", "ETH-EUR", "2017-11-20", "2017-11-21");
        for (StockHistoryView view : result) {
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
