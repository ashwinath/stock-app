package com.ashwinchat.stockapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.ashwinchat.stockapp.batch" })
public class BatchConfig {
}
