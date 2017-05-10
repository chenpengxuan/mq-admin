/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.config;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import com.ymatou.mq.admin.shiro.AdminAuthorizationFilter;
import com.ymatou.mq.admin.shiro.MyAuthorizingRealm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ymatou.mq.admin.shiro.SecurityFilterChainDefinitionSource;


@Configuration
public class ShiroConfig {

    private static Log logger = LogFactory.getLog(ShiroConfig.class);

    @Bean
    public ShiroFilterFactoryBean shiroFilter() throws Exception {

        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("admin", new AdminAuthorizationFilter());

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionSource().getObject());
        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        return securityManager;
    }

    @Bean
    public AuthorizingRealm myRealm() {
        return new MyAuthorizingRealm();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public SecurityFilterChainDefinitionSource filterChainDefinitionSource() {
        return new SecurityFilterChainDefinitionSource();
    }
}
