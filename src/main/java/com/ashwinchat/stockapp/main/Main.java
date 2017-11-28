package com.ashwinchat.stockapp.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ashwinchat.stockapp.bean.TestBean;
import com.ashwinchat.stockapp.config.BatchConfig;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BatchConfig.class);
        TestBean testBean = context.getBean(TestBean.class);
        testBean.helloWorld();
        ((ConfigurableApplicationContext) context).close();
    }

}
