/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.shiro;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.mq.admin.config.BizConfig;
import com.ymatou.mq.admin.exception.BaseRunTimeException;
import com.ymatou.mq.admin.util.CipherUtil;
import com.ymatou.mq.admin.util.LdapHelper;


public class MyAuthorizingRealm extends AuthorizingRealm {

    private static Log logger = LogFactory.getLog(MyAuthorizingRealm.class);

    @Autowired
    private BizConfig bizConfig;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
        String userName = authcToken.getUsername();
        String password = String.valueOf(authcToken.getPassword());
        logger.info("login userName: " + userName);

        String ldapUrl = bizConfig.getLdapUrl();
        if (LdapHelper.authenticate(userName, password, ldapUrl)) {
            return new SimpleAuthenticationInfo(userName, password, getName());
        } else {
            if (StringUtils.isNotBlank(userName) && "admin".equals(userName.trim()) && StringUtils.isNotBlank(password)) {
                if (password.trim().equals("infra@Ymatou1")) {
                    authcToken.setPassword(password.trim().toCharArray());
                    return new SimpleAuthenticationInfo(userName, "infra@Ymatou1", getName());
                } else {
                    throw new BaseRunTimeException("用户名或密码错误");
                }
            }else {
                throw new BaseRunTimeException("用户名或密码错误");
            }
        }

    }
}
