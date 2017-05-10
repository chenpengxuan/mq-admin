/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.exception;

import com.alibaba.fastjson.JSON;
import com.ymatou.mq.admin.util.ResponseStatusEnum;
import com.ymatou.mq.admin.util.Utils;
import com.ymatou.mq.admin.util.WapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author:   lsq
 * Date:     2016/10/30
 * Description:
 */
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(MyHandlerExceptionResolver.class);

    private final static String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    private final static int HTTP_STATUS_OK = 200;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        return null;
    }

    private void responseString(HttpServletResponse response, String jsonpInfo) {

        try {
            response.setContentType(JSON_CONTENT_TYPE);
            response.setStatus(HTTP_STATUS_OK);
            response.getWriter().write(jsonpInfo);

        } catch (IOException e) {
            logger.error("error",e);
        }

    }
}
