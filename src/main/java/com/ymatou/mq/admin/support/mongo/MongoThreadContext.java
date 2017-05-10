/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.support.mongo;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;

import com.mongodb.DBCollection;

/**
 * @author luoshiqian 2017/3/29 13:28
 */
public class MongoThreadContext {

    private static final ThreadLocal<String> COLLECTION_NAME_CTX = new ThreadLocal<>();

    public static void setCollectionName(String dbName) {
        COLLECTION_NAME_CTX.set(dbName);
    }

    public static void clear() {
        COLLECTION_NAME_CTX.set(null);
    }


    public static String getCollectionName() {
        return COLLECTION_NAME_CTX.get();
    }

    public static DBCollection getDbCollection(Datastore datastore, Class clazz) {
        if (StringUtils.isNotBlank(MongoThreadContext.getCollectionName())) {
            return datastore.getDB().getCollection(MongoThreadContext.getCollectionName());
        }
        return datastore.getCollection(clazz);
    }


    public static <T> T supportDynamicCollectionName(String collectionName, Supplier<T> supplier) {
        setCollectionName(collectionName);
        T t = supplier.get();
        clear();

        return t;
    }
}
