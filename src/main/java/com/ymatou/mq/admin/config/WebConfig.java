/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ymatou.mq.admin.exception.MyHandlerExceptionResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.ymatou.mq.admin.exception.MySimpleMappingExceptionResolver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;


@Configuration
//@EnableWebMvc
@ComponentScan(basePackages = "com.ymatou",useDefaultFilters=false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)
        })
public class WebConfig extends WebMvcConfigurerAdapter{

    protected final Log logger = LogFactory.getLog(getClass());


    /**
     * 全局异常处理
     * @return
     */
    @Bean(name="exceptionResolver")
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){

        MySimpleMappingExceptionResolver exceptionHandler= new MySimpleMappingExceptionResolver();

        return exceptionHandler;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new MyHandlerExceptionResolver());
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new ErrorCustomizer();
    }

    private static class ErrorCustomizer implements EmbeddedServletContainerCustomizer {

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage("/common/error.html"));
            container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/common/400.html"));
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/common/403.html"));
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/common/404.html"));
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/common/500.html"));
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.html").addResourceLocations("classpath:/static/html/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("/app/**").addResourceLocations("classpath:/static/app/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

//    @Override
//    protected void addFormatters(FormatterRegistry registry) {
//        // Add formatters and/or converters
//    }
//

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Configure the list of HttpMessageConverters to use
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                Lists.newArrayList(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_HTML,
                        MediaType.TEXT_PLAIN
                        )
        );
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        mappingJackson2HttpMessageConverter.setObjectMapper(mapper);

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        stringHttpMessageConverter.setSupportedMediaTypes(
                Lists.newArrayList(
                        MediaType.TEXT_HTML,
                        MediaType.TEXT_PLAIN
                )
        );

        converters.add(new ResourceHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:index.html");
////        Resource page = this.resourceProperties.getWelcomePage();
////        if (page != null) {
////            logger.info("Adding welcome page: " + page);
////            registry.addViewController("/").setViewName("forward:index.html");
////        }
//
//    }
//
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.enableContentNegotiation(new MappingJackson2JsonView());
//        registry.jsp();
//    }

}
