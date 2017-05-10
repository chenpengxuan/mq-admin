/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.repository;


import static com.ymatou.mq.admin.support.mongo.MongoThreadContext.supportDynamicCollectionName;
import static com.ymatou.mq.admin.util.MongoHelper.getDbName;
import static com.ymatou.mq.admin.util.MongoHelper.getMessageCollectionName;
import static com.ymatou.mq.admin.util.Utils.parseDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.ymatou.mq.admin.config.MongoClientConfig;
import com.ymatou.mq.admin.model.Message;
import com.ymatou.mq.admin.model.MessageCondition;
import com.ymatou.mq.admin.model.QMessage;
import com.ymatou.mq.admin.support.MongoRepository;
import com.ymatou.mq.admin.support.mongo.MorphiaQuery;

import java.util.Date;

/**
 * 消息数据操作 Created by zhangzhihua on 2017/3/23.
 */
@Component("messageRepository")
public class MessageRepository extends MongoRepository {

    @Autowired
    private MongoClientConfig mongoClientConfig;

    private MongoClient mongoClient;

    /**
     * 保存消息
     * 
     * @param msg
     * @return true 保存成功
     */
    public boolean save(Message msg) {
        return insertEntityIngoreDuplicateKey(getDbName(msg.getAppId(), msg.getId()),
                getMessageCollectionName(msg.getQueueCode()),
                msg);
    }

    @Override
    protected MongoClient getMongoClient() {
        return mongoClientConfig.getMongoClientIfNullSet(mongoClient, "messageMongoClient");
    }


    public Message getById(String appId, String queueCode, String id) {
        String dbName = getDbName(appId, id);
        String collectionName = getMessageCollectionName(queueCode);

        return supportDynamicCollectionName(collectionName, () -> {
            QMessage qMessage = new QMessage("");
            MorphiaQuery<Message> query = newQuery(qMessage, dbName);
            Message message = query.where(qMessage.id.eq(id)).fetchOne();
            return message;
        });
    }

    public Page<Message> findByCondition(MessageCondition messageCondition, Pageable pageable) {
        Date startTime = parseDate(messageCondition.getStartTime());
        Date endTime = parseDate(messageCondition.getEndTime());

        String dbName = getDbName(messageCondition.getAppId(),startTime);
        String collectionName = getMessageCollectionName(messageCondition.getQueueCode());

        return supportDynamicCollectionName(collectionName,() -> {

            QMessage qMessage = new QMessage("");
            MorphiaQuery<Message> query = newQuery(qMessage, dbName);
            query.where(qMessage.createTime.after(startTime)
                    .and(qMessage.createTime.before(endTime))
            );
            if(StringUtils.isNotBlank(messageCondition.getBizId())){
                query.where(qMessage.bizId.eq(messageCondition.getBizId()));
            }
           return pager(query,pageable,qMessage.createTime.desc());
        });
    }
}
