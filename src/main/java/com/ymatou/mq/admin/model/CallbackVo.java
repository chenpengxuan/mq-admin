/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

import org.mongodb.morphia.annotations.Id;

/**
 * @author luoshiqian 2017/4/7 11:00
 */
public class CallbackVo{

    @Id
    private CallbackConfig callbackConfig;
    private String appId;
    private String queueCode;

    public CallbackConfig getCallbackConfig() {
        return callbackConfig;
    }

    public void setCallbackConfig(CallbackConfig callbackConfig) {
        this.callbackConfig = callbackConfig;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getQueueCode() {
        return queueCode;
    }

    public void setQueueCode(String queueCode) {
        this.queueCode = queueCode;
    }
}
