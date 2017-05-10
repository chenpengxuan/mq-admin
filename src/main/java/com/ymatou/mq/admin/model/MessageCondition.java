/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

import static com.ymatou.mq.admin.util.Utils.parseDate;

/**
 * @author luoshiqian 2017/3/29 18:24
 */
public class MessageCondition {

    @NotBlank(message = "应用编号必填")
    private String appId;
    @NotBlank(message = "业务代码必填")
    private String queueCode;
    @NotBlank(message = "开始时间必填")
    private String startTime;
    @NotBlank(message = "结束时间必填")
    private String endTime;
    private String status;
    private String bizId;
    private String lastFrom;
    private String callbackKey;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getLastFrom() {
        return lastFrom;
    }

    public void setLastFrom(String lastFrom) {
        this.lastFrom = lastFrom;
    }

    public String getCallbackKey() {
        return callbackKey;
    }

    public void setCallbackKey(String callbackKey) {
        this.callbackKey = callbackKey;
    }
}
