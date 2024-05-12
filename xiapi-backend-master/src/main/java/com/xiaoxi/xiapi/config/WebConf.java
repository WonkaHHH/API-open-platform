package com.xiaoxi.xiapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置映射关系，可通过网络路径访问本地文件
 */
@Configuration
public class WebConf implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user_avatar/**") //虚拟url路径
//                .addResourceLocations("file:D:/projectDev/xi-api/xiapi-backend/user_avatar/"); //真实本地路径
                .addResourceLocations("file:E:/java projects/yupi/casual/api/xiapi-backend-master/user_avatar/");
    }
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //映射static路径的请求到static目录下
//        // 静态资源访问路径和存放路径配置
//        //registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        //通过image访问本地的图片
//        registry.addResourceHandler("/user_avatar/**").addResourceLocations("file:D:/projectDev/xi-api/xiapi-backend/user_avatar/");
//    }

}
