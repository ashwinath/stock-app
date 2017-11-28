package com.ashwinchat.stockapp.main;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.batch.manager.impl.GdaxDownloadManager;
import com.ashwinchat.stockapp.config.BatchConfig;
import com.ashwinchat.stockapp.config.HibernateConfig;
import com.ashwinchat.stockapp.model.view.StockHistoryView;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BatchConfig.class, HibernateConfig.class);
        IDownloadManager gdaxDownloadManager = context.getBean(GdaxDownloadManager.class);
        List<StockHistoryView> result = gdaxDownloadManager.download("", "ETH-EUR", "2017-11-20", "2017-11-21");
        ((ConfigurableApplicationContext) context).close();
    }

}
