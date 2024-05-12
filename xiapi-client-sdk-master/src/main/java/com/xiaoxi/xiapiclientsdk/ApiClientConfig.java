package com.xiaoxi.xiapiclientsdk;

import com.xiaoxi.xiapiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
@ConfigurationProperties("xiapi.client")
@Data
public class ApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public ApiClient apiClient(){
        return new ApiClient(accessKey, secretKey);
    }
}
