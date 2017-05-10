/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */
package com.ymatou.mq.admin.repository;

import static org.mongodb.morphia.aggregation.Group.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.Sort;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.ymatou.mq.admin.config.MongoClientConfig;
import com.ymatou.mq.admin.model.*;
import com.ymatou.mq.admin.support.MongoRepository;
import com.ymatou.mq.admin.support.mongo.MorphiaQuery;

@Component
public class AppConfigRepository extends MongoRepository {

    @Autowired
    private MongoClientConfig mongoClientConfig;
    private MongoClient mongoClient;

    private static final String dbName = "MQ_Configuration_201609";

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.messagebus.infrastructure.mongodb.MongoRepository#getMongoClient()
     */
    @Override
    protected MongoClient getMongoClient() {
        return mongoClientConfig.getMongoClientIfNullSet(mongoClient, "configMongoClient");
    }


    /**
     * 统计出配置App的总数
     * 
     * @return
     */
    public long count() {
        Datastore datastore = getDatastore(dbName);

        Query<AppConfig> find = datastore.find(AppConfig.class);

        return find.countAll();
    }

    /**
     * 返回AppConfig
     * 
     * @return
     */
    public AppConfig getAppConfig(String appId) {
        Datastore datastore = getDatastore(dbName);

        return datastore.find(AppConfig.class).field("_id").equal(appId).get();
    }

    public void deleteById(String appId){
        getDatastore(dbName).delete(AppConfig.class,appId);
    }

    public void saveAppConfig(AppConfig appConfig) {
        Datastore datastore = getDatastore(dbName);
        AppConfig oldAppConfig = this.getAppConfig(appConfig.getAppId());
        if (oldAppConfig != null) {
            UpdateOperations<AppConfig> ups = datastore.createUpdateOperations(AppConfig.class);
            if(StringUtils.isNotBlank(appConfig.getDescription())){
                ups.set("description", appConfig.getDescription());
            }

            if(StringUtils.isNotBlank(appConfig.getDispatchGroup())){
                ups.set("dispatchGroup", appConfig.getDispatchGroup());
            }

            if(appConfig.getMqType() != null){
                ups.set("mqType", appConfig.getMqType());
            }

            datastore.update(datastore.createQuery(AppConfig.class).field("appId").equal(appConfig.getAppId()), ups);
        } else {
            insertEntiy(dbName, appConfig);
        }
    }

    public void saveQueueConfig(String appId,QueueConfig queueConfig){
        Datastore datastore = getDatastore(dbName);
        AppConfig appConfig = this.getAppConfig(appId);
        QueueConfig oldQueueConfig = appConfig.getMessageConfig(queueConfig.getCode());
        UpdateOperations<AppConfig> ups = datastore.createUpdateOperations(AppConfig.class);

        if(null == oldQueueConfig){
            ups.add("messageCfgList",queueConfig);
            datastore.update(datastore.createQuery(AppConfig.class).field("appId").equal(appConfig.getAppId()), ups);
        }else {

            //Updating subdocument http://stackoverflow.com/questions/18173482/mongodb-update-deeply-nested-subdocument
            // http://stackoverflow.com/questions/5646798/mongodb-updating-subdocument
            ups.set("messageCfgList.$.enable",queueConfig.getEnable());
            ups.set("messageCfgList.$.enableLog",queueConfig.getEnableLog());
            ups.set("messageCfgList.$.poolSize",queueConfig.getPoolSize());
            ups.set("messageCfgList.$.checkCompensateDelay",queueConfig.getCheckCompensateDelay());
            ups.set("messageCfgList.$.checkCompensateTimeSpan",queueConfig.getCheckCompensateTimeSpan());
            ups.set("messageCfgList.$.description",queueConfig.getDescription());

            datastore.update(
                    datastore.createQuery(AppConfig.class)
                    .field("appId").equal(appConfig.getAppId())
                    .field("messageCfgList.code").equal(queueConfig.getCode())
                    , ups);
        }

    }


    public void saveCallbackConfig(String appId,String queueCode,CallbackConfig callbackConfig){
        Datastore datastore = getDatastore(dbName);
        AppConfig appConfig = this.getAppConfig(appId);
        QueueConfig queueConfig = appConfig.getMessageConfig(queueCode);

        UpdateOperations<AppConfig> ups = datastore.createUpdateOperations(AppConfig.class);
        if(StringUtils.isBlank(callbackConfig.getCallbackKey())){
            AtomicInteger maxSequence = new AtomicInteger(0);
            String prefix = appId + "_" + queueCode + "_c";
            queueConfig.getCallbackCfgList().stream().forEach(c -> {
                int sequence = Integer.valueOf(c.getCallbackKey().replace(prefix,""));
                if(maxSequence.get() <= sequence){
                    maxSequence.set(sequence + 1);
                }
            });
            //设置主键
            callbackConfig.setCallbackKey(prefix + maxSequence.get());

            //新增
            ups.add("messageCfgList.$.callbackCfgList",callbackConfig);
            datastore.update(
                    datastore.createQuery(AppConfig.class)
                            .field("appId").equal(appId)
                            .field("messageCfgList.code").equal(queueCode)
                    , ups);
        }else {


            Iterables.removeIf(queueConfig.getCallbackCfgList(),call -> call.getCallbackKey().equals(callbackConfig.getCallbackKey()));
            queueConfig.getCallbackCfgList().add(callbackConfig);

            //全部替换
            ups.set("messageCfgList.$.callbackCfgList",queueConfig.getCallbackCfgList());
            datastore.update(
                    datastore.createQuery(AppConfig.class)
                            .field("appId").equal(appId)
                            .field("messageCfgList.code").equal(queueCode)
                    , ups);
//            ups.disableValidation();
//            DBObject u  = ((CustomDatastoreImpl) datastore).getMapper().toDBObject(callbackConfig);
//            u.removeField("className");
//            ups.set("MessageCfgList.$.CallbackCfgList.$",u); //为了去掉className  这里使用大写 MessageCfgList.$.CallbackCfgList 是和数据库一致，没有使用morphia 字段映射
//
//            datastore.update(
//                    datastore.createQuery(AppConfig.class)
//                            .field("appId").equal(appId)
//                            .field("messageCfgList.code").equal(queueCode)
//                            .field("messageCfgList.callbackCfgList.callbackKey").equal(callbackConfig.getCallbackKey())
//                    , ups);
        }
    }

    /**
     * @return
     */
    public List<AppConfig> getAllAppConfig() {
        Datastore datastore = getDatastore(dbName);

        return datastore.find(AppConfig.class).asList();
    }

    public Page<AppConfig> findByPage(String appId, String groupId, Pageable pageable) {
        QAppConfig qAppConfig = new QAppConfig("");

        MorphiaQuery query = newQuery(qAppConfig, dbName);

        if (StringUtils.isNotBlank(appId)) {
            query.where(qAppConfig.appId.eq(appId));
        }
        if (StringUtils.isNotBlank(groupId)) {
            query.where(qAppConfig.dispatchGroup.eq(groupId));
        }

        return pager(query, pageable);
    }

    public Page<CallbackVo> findCallbackPage(CallbackCondition condition, Pageable pageable) {

        Query q = newQuery(Function.class,dbName,"MQ_App_Cfg", ReadPreference.primaryPreferred());
        //由于是先group 再match所有 使用的是group之后的field
        q.disableValidation();
        if(StringUtils.isNotBlank(condition.getAppId())){
            q.field("appId").equal(condition.getAppId());
        }

        if(StringUtils.isNotBlank(condition.getQueueCode())){
            q.field("queueCode").equal(condition.getQueueCode());
        }

        //对分组后_id里Url模糊查询
        if(StringUtils.isNotBlank(condition.getCallbackUrl())){
//            Pattern pattern = Pattern.compile(condition.getCallbackUrl(), Pattern.CASE_INSENSITIVE);
//            q.getQueryObject().put("_id.Url",pattern);
            /**
             *  不支持多级查询 所以使用的一个接口类型进去 Function.class  传入接口类型 会跳过下面的异常
             *  QueryValidator throw new ValidationException(format("The field '%s' could not be found in '%s'", prop, mc.getClazz().getName()));
             */

            q.criteria("_id.Url").containsIgnoreCase(condition.getCallbackUrl());
        }

        long count = findGroupCount(newFindCallbackPipeline(q));
        Page page;
        if(count > 0){
            Iterator iterator = newFindCallbackPipeline(q)
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .aggregate(CallbackVo.class);

            List<CallbackVo> callbackVos = Lists.newArrayList();
            while (iterator.hasNext()){
                CallbackVo callbackVo = (CallbackVo)iterator.next();
                callbackVos.add(callbackVo);
            }
            page = new PageImpl<>(callbackVos,pageable,count);
        }else {
            page = new PageImpl<>(new ArrayList<>(),pageable,count);
        }
        return page;
    }

    private AggregationPipeline newFindCallbackPipeline(Query q){
        AggregationPipeline pipeline = createPipeline(dbName,AppConfig.class);
        pipeline.unwind("MessageCfgList")
                .unwind("MessageCfgList.CallbackCfgList")
                .group("_id",grouping("_id","MessageCfgList.CallbackCfgList"),
                        grouping("appId", first("_id")),
                        grouping("queueCode", first("MessageCfgList.Code")),grouping("count",new Accumulator("$sum",1)))
                .match(q);
        return pipeline;
    }

    private long findGroupCount(AggregationPipeline pipeline){
        Iterator iterator = pipeline.group(Lists.newArrayList(),grouping("total",sum("count"))).aggregate(GroupCountVo.class);
        if(iterator.hasNext()){
            GroupCountVo groupCountVo = (GroupCountVo)iterator.next();
            return groupCountVo.getTotal();
        }
        return 0;
    }
}
