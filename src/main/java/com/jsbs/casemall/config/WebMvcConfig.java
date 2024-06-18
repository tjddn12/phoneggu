package com.jsbs.casemall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;
    @Value("${productImgLocation}")
    private String productImgLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/mainImg");
        // URL 패턴이 /images/**로 시작하는 경우
        // 1. 로컬 파일 시스템에서 파일을 제공
        // 2. 클래스패스의 static/images 디렉토리에서 파일을 제공

        registry.addResourceHandler("/images/product/**")
                .addResourceLocations("file:///" + uploadPath + "/")
                .addResourceLocations("file:" + productImgLocation + "/");

        registry.addResourceHandler("/order/pay/**")
                .addResourceLocations("classpath:/templates/pay/")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
        // URL 패턴이 /order/pay/**로 시작하는 경우
        // 클래스패스의 templates/pay 디렉토리에서 파일을 제공
        // 클래스패스의 static 디렉토리에서 파일을 제공
        // 브라우저가 리소스를 10분 동안 캐시하도록 설정
    }


}