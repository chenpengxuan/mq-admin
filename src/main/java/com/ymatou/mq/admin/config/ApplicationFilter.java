/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;


@Configuration
public class ApplicationFilter {

    /**
     * 编码配置
     * @return
     */
    @Bean
    public FilterRegistrationBean encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("utf-8");
        FilterRegistrationBean mappingEncodingFilter = new FilterRegistrationBean(encodingFilter);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        mappingEncodingFilter.setUrlPatterns(urlPatterns);
        mappingEncodingFilter.setOrder(1);

        return mappingEncodingFilter;
    }

//    @Bean
//    public FilterRegistrationBean urlFilter() {
//        UrlFilter urlFilter = new UrlFilter();
//
//        FilterRegistrationBean filter = new FilterRegistrationBean(urlFilter);
//
//        List<String> urlPatterns = new ArrayList<String>();
//        urlPatterns.add("/*");
//        filter.setUrlPatterns(urlPatterns);
//        filter.setOrder(2);
//
//        return filter;
//    }

}
