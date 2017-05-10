/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.model.enums;

/**
 * 回调来源
 * @author zhangyifan 2016/9/1 12:00
 */
public enum CallbackFromEnum {


    /**
     * 初始化
     */
    INIT(0),

    /**
     * 分发站
     */
    DISPATCH(1),


    /**
     * 补单站
     */
    COMPENSATE(2),;

    private int code;

    CallbackFromEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

