package com.xiaoxi.xiapiinterface.config;

import io.github.briqt.spark4j.SparkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SparkConfig {

    @Value("${spark.client.appid}")
    private String appid;

    @Value("${spark.client.apiKey}")
    private String apiKey;

    @Value("${spark.client.apiSecret}")
    private String apiSecret;

    @Bean
    public SparkClient sparkClient(){
        SparkClient sparkClient = new SparkClient();
        // 设置认证信息
        sparkClient.appid = appid;
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        return sparkClient;
    }
}
