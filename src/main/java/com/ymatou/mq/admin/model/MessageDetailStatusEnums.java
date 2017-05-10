/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

/**
 * 消息明细状态枚举
 * @author luoshiqian 2017/4/6 16:24
 */
public enum MessageDetailStatusEnums {

    INIT(0), SUCCESS(1), FAIL(2), COMPENSATE(3),;

    int code;

    MessageDetailStatusEnums(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MessageDetailStatusEnums findByCode(int code){
        for (MessageDetailStatusEnums val :values()){
            if(val.getCode() == code){
                return val;
            }
        }
        return MessageDetailStatusEnums.INIT;
    }
}
