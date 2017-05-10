/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.support.mongo;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.AggregationPipelineImpl;

/**
 * @author luoshiqian 2017/4/6 15:54
 */
public class CustomDatastoreImpl extends DatastoreImpl {
    public CustomDatastoreImpl(Morphia morphia, MongoClient mongoClient, String dbName) {
        super(morphia, mongoClient, dbName);
    }

    @Override
    public DBCollection getCollection(Class clazz) {

        if (StringUtils.isNotBlank(MongoThreadContext.getCollectionName())) {
            return getDB().getCollection(MongoThreadContext.getCollectionName());
        }
        return super.getCollection(clazz);
    }

    public DBCollection getCollection() {

        if (StringUtils.isNotBlank(MongoThreadContext.getCollectionName())) {
            return getDB().getCollection(MongoThreadContext.getCollectionName());
        }

        throw new RuntimeException("collection name is not set!!");
    }
}
