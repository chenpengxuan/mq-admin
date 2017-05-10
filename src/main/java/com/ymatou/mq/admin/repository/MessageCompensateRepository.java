/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.repository;


import static com.ymatou.mq.admin.util.MongoHelper.getMessageCompensateCollectionName;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.ymatou.mq.admin.config.MongoClientConfig;
import com.ymatou.mq.admin.model.MessageCompensate;
import com.ymatou.mq.admin.support.MongoRepository;


/**
 * 消息补单数据操作 Created by zhangzhihua on 2017/3/24.
 */
@Component("messageCompensateRepository")
public class MessageCompensateRepository extends MongoRepository {

    @Autowired
    private MongoClientConfig mongoClientConfig;

    private MongoClient mongoClient;

    private static final String dbName = "JMQ_V2_Message_Compensate";

    @Override
    protected MongoClient getMongoClient() {
        return mongoClientConfig.getMongoClientIfNullSet(mongoClient, "messageCompensateMongoClient");
    }


    public List<MessageCompensate> findByQueueCodeAndIdList(String appId,String queueCode, List<String> idList) {
        String collectionName = getMessageCompensateCollectionName(appId,queueCode);

        Query<MessageCompensate> query =
                newQuery(MessageCompensate.class, dbName, collectionName, ReadPreference.primaryPreferred());
        query.field("id").in(idList);

        return query.asList();
    }


}
