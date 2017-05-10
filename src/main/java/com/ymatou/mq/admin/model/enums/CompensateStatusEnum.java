/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.model.enums;

/**
 * 补单状态
 * @author zhangyifan 2016/9/1 12:00
 */
public enum CompensateStatusEnum {


    /**
     * 补单中
     */
    COMPENSATE(0),

    /**
     * 成功
     */
    SUCCESS(1),


    /**
     * 失败
     */
    FAIL(2);


    private int code;

    CompensateStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

