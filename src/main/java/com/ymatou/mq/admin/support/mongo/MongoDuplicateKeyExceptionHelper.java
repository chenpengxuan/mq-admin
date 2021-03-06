/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.support.mongo;

import com.mongodb.MongoException;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author luoshiqian 2017/3/27 18:24
 */
public class MongoDuplicateKeyExceptionHelper {

    private static final Set<String> DULICATE_KEY_EXCEPTIONS = new HashSet<String>(
            Arrays.asList("MongoException.DuplicateKey", "DuplicateKeyException"));


    /**
     * 判断是否DuplicateKey异常
     * @param ex
     * @return
     */
    public static boolean isDuplicateKeyException(RuntimeException ex){
        String exception = ClassUtils.getShortName(ClassUtils.getUserClass(ex.getClass()));

        if (DULICATE_KEY_EXCEPTIONS.contains(exception)) {
            return true;
        }

        if (ex instanceof MongoException) {
            int code = ((MongoException) ex).getCode();
            if (MongoDbErrorCodes.isDuplicateKeyCode(code)) {
                return true;
            }
        }
        return false;
    }
}
