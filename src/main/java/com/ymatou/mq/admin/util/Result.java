/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;



public class Result {

    private boolean success;

    private String message;

    private Object content;

    public Result(){}

    public Result(Object content){
        this.content = content;
    }

    public Result(ResponseStatusEnum statusEnum){
        this.success = statusEnum.isSuccess();
        this.message = statusEnum.getStatusInfo();
    }

    public Result(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public Result(ResponseStatusEnum statusEnum, Object content){
        this.success = statusEnum.isSuccess();
        this.message = statusEnum.getStatusInfo();
        this.content = content;
    }

    public Result(boolean success, String message, Object content){
        this.success = success;
        this.message = message;
        this.content = content;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * 静态方法 参数错误
     * @param message
     * @return
     */
    public static Result paramError(String message){
        return new Result(ResponseStatusEnum.ERROR.isSuccess(),message);
    }

    /**
     * 静态方法 成功
     * @param message
     * @return
     */
    public static Result success(String message){
        return new Result(ResponseStatusEnum.SUCCESS.isSuccess(),message);
    }
}
