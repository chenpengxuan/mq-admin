/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;


import org.apache.commons.lang3.StringUtils;

public class WapperUtil {

    public static Result success(){
        return success(null);
    }
    public static Result success(String msg){
        return success(msg,null);
    }
    public static Result success(Object data){
        return success("",data);
    }
    public static Result success(String msg,Object data){
        return newInstance(ResponseStatusEnum.SUCCESS.isSuccess(),
                StringUtils.isBlank(msg) ? ResponseStatusEnum.SUCCESS.getStatusInfo() : msg, data);
    }
    public static Result error(){
        return error(null, null);
    }

    public static Result error(String message){
        return error(null, message);
    }

    public static Result result(boolean success,String message){
        return newInstance(success, message, null);
    }

    public static Result error(Object data,String message){
        return newInstance(ResponseStatusEnum.ERROR.isSuccess(),message,data);
    }

    public static Result newInstance(boolean success,String message,Object data){
        return new Result(success,message,data);
    }
}
