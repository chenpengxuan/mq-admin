/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

/**
 * @author luoshiqian 2017/4/11 15:05
 */
public enum RoleEnums {

    USER(1),ADMIN(9);

    int code;

    RoleEnums(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
