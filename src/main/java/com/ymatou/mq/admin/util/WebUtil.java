/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

public class WebUtil {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (null == requestAttributes) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletWebRequest webRequest = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        if(null == webRequest){
            return null;
        }
        return webRequest.getResponse();
    }

}
