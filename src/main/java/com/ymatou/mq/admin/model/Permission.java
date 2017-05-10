/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ymatou.mq.admin.util.Constants;

/**
 * @author luoshiqian 2017/4/11 15:07
 */
@Entity(value = "MQ_Permission", noClassnameStored = true)
public class Permission {

    @Id
    private ObjectId id;

    @Property("LoginName")
    private String loginName;

    @Property("Action")
    private int action;

    @Property("Enable")
    private int enable;

    @JsonFormat(pattern = Constants.DATE_FORMAT_YMD_HMS)
    @Property("CreateTime")
    private Date createTime;

    @JsonFormat(pattern = Constants.DATE_FORMAT_YMD_HMS)
    @Property("UpdateTime")
    private Date updateTime;

    @Property("Creator")
    private String creator;

    @Property("Updator")
    private String updator;


    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

}
