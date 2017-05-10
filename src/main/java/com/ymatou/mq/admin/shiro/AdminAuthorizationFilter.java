/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ymatou.mq.admin.model.User;
import com.ymatou.mq.admin.util.CurrentUserUtil;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;



public class AdminAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        User user = CurrentUserUtil.getCurrentUser();

        if(user != null){
            return true;
        }

        return false;
    }

}
