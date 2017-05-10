/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;


public enum ResponseStatusEnum {

    SUCCESS(true,"请求成功！"),
    ERROR(false,"请求失败！");

    private String statusInfo;
    private boolean success;

    ResponseStatusEnum(boolean success,String statusInfo){
        this.statusInfo = statusInfo;this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStatusInfo() {
        return statusInfo;
    }
}
