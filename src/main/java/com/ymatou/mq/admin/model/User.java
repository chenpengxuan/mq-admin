/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

import com.baidu.disconf.client.DisConf;
import com.baidu.disconf.client.config.DisClientConfig;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author luoshiqian 2016/7/28 12:53
 */

public class User {

    protected Long id;


    protected String username;


    protected String password;


    protected String email;


    protected long role = RoleEnums.USER.getCode();

    protected String env;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getEnv() {
        return DisClientConfig.getInstance().ENV;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
