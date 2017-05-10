/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.repository;


import static com.ymatou.mq.admin.support.mongo.MongoThreadContext.supportDynamicCollectionName;
import static com.ymatou.mq.admin.util.MongoHelper.getDbName;
import static com.ymatou.mq.admin.util.MongoHelper.getMessageCollectionName;
import static com.ymatou.mq.admin.util.MongoHelper.getMessageDetailCollectionName;
import static com.ymatou.mq.admin.util.Utils.parseDate;
import static org.mongodb.morphia.aggregation.Group.first;
import static org.mongodb.morphia.aggregation.Group.grouping;
import static org.mongodb.morphia.aggregation.Group.id;
import static com.mongodb.AggregationOptions.builder;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.mongodb.AggregationOptions;
import com.mongodb.ReadPreference;
import com.ymatou.mq.admin.model.*;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.ymatou.mq.admin.config.MongoClientConfig;
import com.ymatou.mq.admin.support.MongoRepository;
import com.ymatou.mq.admin.support.mongo.MorphiaQuery;


/**
 * 消息分发明细数据操作 Created by zhangzhihua on 2017/3/24.
 */
@Component("messageDispatchDetailRepository")
public class MessageDispatchDetailRepository extends MongoRepository {

    @Autowired
    private MongoClientConfig mongoClientConfig;

    private MongoClient mongoClient;

    /**
     * 保存分发明细
     * 
     * @param detail
     */
    public boolean saveDetail(MessageDispatchDetail detail) {
        return insertEntityIngoreDuplicateKey(getDbName(detail.getAppId(), detail.getMsgId()),
                getMessageDetailCollectionName(detail.getQueueCode()),
                detail);
    }



    @Override
    protected MongoClient getMongoClient() {
        return mongoClientConfig.getMongoClientIfNullSet(mongoClient, "messageMongoClient");
    }


    public List<MessageDispatchDetail> findByMsgId(String appId, String queueCode, String msgId) {
        QMessageDispatchDetail queryModel = new QMessageDispatchDetail("");

        String dbName = getDbName(appId, msgId);
        String collectionName = getMessageDetailCollectionName(queueCode);

        return supportDynamicCollectionName(collectionName, () -> {
            MorphiaQuery<MessageDispatchDetail> query = newQuery(queryModel, dbName);
            query.where(
                    queryModel.appId.eq(appId).and(queryModel.queueCode.eq(queueCode)).and(queryModel.msgId.eq(msgId)));
            return query.fetch();
        });

    }

    public Page<MessageDispatchDetail> findByCondition(MessageCondition messageCondition, Pageable pageable){
        Date startTime = parseDate(messageCondition.getStartTime());
        Date endTime = parseDate(messageCondition.getEndTime());

        String dbName = getDbName(messageCondition.getAppId(),startTime);
        String collectionName = getMessageDetailCollectionName(messageCondition.getQueueCode());

        return supportDynamicCollectionName(collectionName,() -> {

            QMessageDispatchDetail qMessage = new QMessageDispatchDetail("");
            MorphiaQuery<MessageDispatchDetail> query = newQuery(qMessage, dbName);
            query.where(qMessage.createTime.after(startTime)
                    .and(qMessage.createTime.before(endTime))
            );
            if(StringUtils.isNotBlank(messageCondition.getBizId())){
                query.where(qMessage.bizId.eq(messageCondition.getBizId()));
            }

            if(StringUtils.isNotBlank(messageCondition.getStatus())){
                query.where(qMessage.status.eq(Integer.valueOf(messageCondition.getStatus())));
            }

            if(StringUtils.isNotBlank(messageCondition.getLastFrom())){
                query.where(qMessage.lastFrom.eq(Integer.valueOf(messageCondition.getLastFrom())));
            }

            if(StringUtils.isNotBlank(messageCondition.getCallbackKey())){
                query.where(qMessage.consumerId.containsIgnoreCase(messageCondition.getCallbackKey()));
            }

            return pager(query,pageable,qMessage.createTime.desc());
        });
    }

    /**
     * 返回每个消息分发明细状态 数量
     * 
     * @param appId
     * @param queueCode
     * @param msgIdList
     * @return
     */
    public List<MessageDetailGroup> findStatusByMsgIds(String appId, String queueCode, List<String> msgIdList) {
        String dbName = getDbName(appId, msgIdList.get(0));
        String collectionName = getMessageDetailCollectionName(queueCode);


        return supportDynamicCollectionName(collectionName,() -> {
            Query query = newQuery(MessageDispatchDetail.class,dbName,collectionName,ReadPreference.primaryPreferred())
                    .field("msgId").in(msgIdList);

            AggregationPipeline pipeline = createPipeline(dbName,MessageDispatchDetail.class);
            Iterator iterator = pipeline
                    .match(query)
                    .group(id(grouping("msgId"), grouping("status")),
                            grouping("msgId", first("msgId")),
                            grouping("status", first("status")),
                            grouping("count", new Accumulator("$sum", 1)))
                    .aggregate(MessageDetailGroup.class)
                    ;
            List<MessageDetailGroup> messageDetailGroups = Lists.newArrayList();
            while (iterator.hasNext()){
                messageDetailGroups.add((MessageDetailGroup)iterator.next());
            }
            return messageDetailGroups;
        });
    }
}
