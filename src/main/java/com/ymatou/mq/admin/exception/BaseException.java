/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.exception;

public class BaseException extends Exception {

    public BaseException(){
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause){
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

}
