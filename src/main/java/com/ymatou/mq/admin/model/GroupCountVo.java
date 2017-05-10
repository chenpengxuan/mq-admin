/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

import org.mongodb.morphia.annotations.Id;

/**
 * @author luoshiqian 2017/4/7 11:45
 */
public class GroupCountVo {

    @Id
    private Object id;

    private long total;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
