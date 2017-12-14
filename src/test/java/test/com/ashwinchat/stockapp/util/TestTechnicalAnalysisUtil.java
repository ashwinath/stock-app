package test.com.ashwinchat.stockapp.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ashwinchat.stockapp.model.view.StockHistoryView;
import com.ashwinchat.stockapp.util.TechnicalAnalysisUtil;

public class TestTechnicalAnalysisUtil {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSmaCalc() {
        List<StockHistoryView> histViews = this.genHistoryList();
        BigDecimal sma = TechnicalAnalysisUtil.calcSma(histViews, StockHistoryView::getClose);
        Assert.assertEquals(new BigDecimal(6), sma);
    }

    @Test
    public void testSmaCalcNullList() {
        this.thrown.expect(IllegalArgumentException.class);
        TechnicalAnalysisUtil.calcSma(null, StockHistoryView::getClose);
    }

    @Test
    public void testSmaNullGetter() {
        this.thrown.expect(IllegalArgumentException.class);
        List<StockHistoryView> histViews = this.genHistoryList();
        TechnicalAnalysisUtil.calcSma(histViews, null);
    }

    @Test
    public void testMin() {
        List<StockHistoryView> histViews = this.genHistoryList();
        BigDecimal min = TechnicalAnalysisUtil.getMin(histViews, StockHistoryView::getClose);
        Assert.assertEquals(new BigDecimal(2), min);
    }

    @Test
    public void testMinNullList() {
        this.thrown.expect(IllegalArgumentException.class);
        TechnicalAnalysisUtil.getMin(null, StockHistoryView::getClose);
    }

    @Test
    public void testMinNullGetter() {
        this.thrown.expect(IllegalArgumentException.class);
        List<StockHistoryView> histViews = this.genHistoryList();
        TechnicalAnalysisUtil.getMin(histViews, null);
    }

    @Test
    public void testMax() {
        List<StockHistoryView> histViews = this.genHistoryList();
        BigDecimal max = TechnicalAnalysisUtil.getMax(histViews, StockHistoryView::getClose);
        Assert.assertEquals(BigDecimal.TEN, max);
    }

    @Test
    public void testMaxNullList() {
        this.thrown.expect(IllegalArgumentException.class);
        TechnicalAnalysisUtil.getMax(null, StockHistoryView::getClose);
    }

    @Test
    public void testMaxNullGetter() {
        this.thrown.expect(IllegalArgumentException.class);
        List<StockHistoryView> histViews = this.genHistoryList();
        TechnicalAnalysisUtil.getMax(histViews, null);
    }

    private List<StockHistoryView> genHistoryList() {
        StockHistoryView histView1 = new StockHistoryView();
        histView1.setClose(BigDecimal.TEN);
        histView1.setHigh(BigDecimal.TEN);
        histView1.setLow(BigDecimal.TEN);
        histView1.setOpen(BigDecimal.TEN);
        histView1.setVolume(BigDecimal.TEN);

        StockHistoryView histView2 = new StockHistoryView();
        histView2.setClose(new BigDecimal(2));
        histView2.setHigh(new BigDecimal(2));
        histView2.setLow(new BigDecimal(2));
        histView2.setOpen(new BigDecimal(2));
        histView2.setVolume(new BigDecimal(2));

        return Arrays.asList(histView1, histView2);
    }
}
