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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class MySimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private final static String HEADER_STRING = "X-Requested-With";
    private final static String AJAX_HEADER = "XMLHttpRequest";
    private final static String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    private final static int HTTP_STATUS_OK = 200;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        boolean isAjax = AJAX_HEADER.equals(request.getHeader(HEADER_STRING));

        AtomicReference<String> message = new AtomicReference<>("");


        if (isAjax) {
            if (ex instanceof BindException ) {
                BindingResult bindingResult = ((BindException) ex).getBindingResult();
                bindingResult.getFieldErrors().stream().forEach(fieldError -> {
                    message.set(message.get() + fieldError.getDefaultMessage()  + "&");
                });
            }else {
                message.set(Utils.getStackTrace(ex));
                logger.error("exception :",ex);
            }
            responseString(response,JSON.toJSONString(WapperUtil.error(message.get())));
            return new ModelAndView();
        }else {
            logger.error("exception :",ex);
        }

        return super.doResolveException(request, response, handler, ex);
    }

    private void responseString(HttpServletResponse response, String jsonpInfo) {

        try {
            response.setContentType(JSON_CONTENT_TYPE);
            response.setStatus(HTTP_STATUS_OK);
            response.getWriter().write(jsonpInfo);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
