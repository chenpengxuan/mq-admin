/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.model;

/**
 * @author luoshiqian 2017/4/6 16:19
 */
public class MessageVo extends Message {

    private int count;// 总数

    private int initCount;// 初始化状态数

    private int successCount;// 成功状态数

    private int failCount;// 失败状态数

    private int compensateCount;// 补单中状态数

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getInitCount() {
        return initCount;
    }

    public void setInitCount(int initCount) {
        this.initCount = initCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getCompensateCount() {
        return compensateCount;
    }

    public void setCompensateCount(int compensateCount) {
        this.compensateCount = compensateCount;
    }
}
