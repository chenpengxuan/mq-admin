/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.config;

import com.ymatou.mq.admin.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author luoshiqian 2016/8/2 12:48
 */
public class UrlFilter extends OncePerRequestFilter {

    public static final Logger logger = LoggerFactory.getLogger(UrlFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 初始化会话
        ThreadContext.init();
        logger.info("request url:{}",request.getRequestURL());
    }
}
