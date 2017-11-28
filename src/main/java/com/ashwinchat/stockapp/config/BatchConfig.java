package com.ashwinchat.stockapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ashwinchat.stockapp.bean.TestBean;

@Configuration
public class BatchConfig {

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }
}
