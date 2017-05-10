/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ymatou.mq.admin.util.Constants;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 消息模型 Created by zhangzhihua on 2017/3/23.
 */
@Entity(noClassnameStored = true)
public class Message extends PrintFriendliness{


    /**
     * 唯一标识
     */
    @Property("_id")
    @Id
    private String id;

    /**
     * 应用id
     */
    @Property("appId")
    private String appId;

    /**
     * 队列code
     */
    @Property("queueCode")
    private String queueCode;

    /**
     * 消息id,客户端消息标识
     */
    @Property("bizId")
    private String bizId;

    /**
     * 消息体
     */
    @Property("body")
    private String body;

    /**
     * 发送方客户端ip
     */
    @Property("clientIp")
    private String clientIp;

    /**
     * 接收站ip
     */
    @Property("recvIp")
    private String recvIp;

    /**
     * 从MQ消费时间
     */
    @Property("consumeTime")
    @JsonFormat(pattern = Constants.DATE_FORMAT_YMD_HMS)
    private Date consumeTime;

    /**
     * 创建时间
     */
    @Property("createTime")
    @JsonFormat(pattern = Constants.DATE_FORMAT_YMD_HMS)
    private Date createTime;

    /**
     * 最后更新时间
     */
    @Property("updateTime")
    @JsonFormat(pattern = Constants.DATE_FORMAT_YMD_HMS)
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getRecvIp() {
        return recvIp;
    }

    public void setRecvIp(String recvIp) {
        this.recvIp = recvIp;
    }

    public Date getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Date consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static String toJsonString(Message message) {
        return JSON.toJSONString(message);
    }

    public static Message fromJson(String message) {
        return JSON.parseObject(message, Message.class);
    }


}
