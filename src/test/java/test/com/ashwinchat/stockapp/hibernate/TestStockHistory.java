package test.com.ashwinchat.stockapp.hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.pk.StockPrimaryKey;
import com.ashwinchat.stockapp.model.view.StockHistoryView;

public class TestStockHistory extends HibernateCrudTest {
    private LocalDateTime now = LocalDateTime.now();
    private String stockTyp = "StockType";
    private String stockName = "StockName";

    @Autowired
    @Qualifier("genericDao")
    private IDao<StockHistoryView> stockHistoryDao;

    @Override
    @Test
    public void testUpdate() {
        StockHistoryView view = insertRecord();
        view.setHigh(new BigDecimal(123));
        this.stockHistoryDao.update(view);
        Query<StockHistoryView> query = this.stockHistoryDao.createQuery(
                "from StockHistoryView where pk.stockTyp = :stockTyp and pk.stockName = :stockName and pk.epochTime = :epochTime");
        query.setParameter("stockTyp", this.stockTyp);
        query.setParameter("stockName", this.stockName);
        query.setParameter("epochTime", this.now.atZone(ZoneId.systemDefault()).toEpochSecond());
        List<StockHistoryView> results = query.getResultList();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(new BigDecimal(123), results.get(0).getHigh());
    }

    @Override
    @Test
    public void testInsert() {
        this.insertRecord();
        Query<StockHistoryView> query = this.stockHistoryDao.createQuery(
                "from StockHistoryView where pk.stockTyp = :stockTyp and pk.stockName = :stockName and pk.epochTime = :epochTime");
        query.setParameter("stockTyp", this.stockTyp);
        query.setParameter("stockName", this.stockName);
        query.setParameter("epochTime", this.now.atZone(ZoneId.systemDefault()).toEpochSecond());
        List<StockHistoryView> results = query.getResultList();
        Assert.assertEquals(1, results.size());
    }

    @Override
    @Test
    public void queryNonExistent() {
        Query<StockHistoryView> query = this.stockHistoryDao.createQuery(
                "from StockHistoryView where pk.stockTyp = :stockTyp and pk.stockName = :stockName and pk.epochTime = :epochTime");
        query.setParameter("stockTyp", "lul");
        query.setParameter("stockName", "lul");
        query.setParameter("epochTime", this.now.atZone(ZoneId.systemDefault()).toEpochSecond());
        List<StockHistoryView> results = query.getResultList();
        Assert.assertEquals(0, results.size());
    }

    private StockHistoryView insertRecord() {
        StockHistoryView view = new StockHistoryView();
        view.setClose(new BigDecimal(999));
        view.setHigh(new BigDecimal(999));
        view.setLow(new BigDecimal(999));
        view.setOpen(new BigDecimal(999));
        view.setVolume(new BigDecimal(999999));
        view.setTime(this.now);

        StockPrimaryKey pk = new StockPrimaryKey();
        pk.setStockName(this.stockName);
        pk.setStockTyp(this.stockTyp);
        pk.setEpochTime(this.now.atZone(ZoneId.systemDefault()).toEpochSecond());
        view.setPk(pk);
        this.stockHistoryDao.save(view);

        return view;
    }

}
