package com.ashwinchat.stockapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ashwinchat.stockapp.batch.manager.IDownloadManager;
import com.ashwinchat.stockapp.batch.manager.impl.GdaxDownloadManager;

@Configuration
public class BatchConfig {

    @Bean(name = "gdaxDownloadManager")
    public IDownloadManager gdaxDownloadManager() {
        return new GdaxDownloadManager();
    }
}
