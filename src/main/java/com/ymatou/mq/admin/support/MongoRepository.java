/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */
package com.ymatou.mq.admin.support;


import static com.ymatou.mq.admin.support.mongo.MongoDuplicateKeyExceptionHelper.isDuplicateKeyException;

import java.util.ArrayList;
import java.util.Set;

import com.querydsl.core.types.OrderSpecifier;
import com.ymatou.mq.admin.model.Message;
import com.ymatou.mq.admin.support.mongo.CustomDatastoreImpl;
import org.bson.conversions.Bson;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.querydsl.core.types.EntityPath;
import com.ymatou.mq.admin.support.mongo.MorphiaQuery;


/**
 * Mongo仓储基类
 * 
 * @author wangxudong 2016年8月1日 下午12:58:11
 *
 */
public abstract class MongoRepository {

    protected Morphia morphia = new Morphia().mapPackage("com.ymatou.mq.admin.model");


    /**
     * 获取到MongoClient
     * 
     * @return
     */
    protected abstract MongoClient getMongoClient();

    /**
     * 获取到制定库名的数据源
     *
     * @param dbName
     * @return
     */
    protected Datastore getDatastore(String dbName) {
        return new CustomDatastoreImpl(morphia,getMongoClient(), dbName);
    }

    /**
     * 获取到集合名称
     * 
     * @param dbName
     * @return
     */
    protected Set<String> getCollectionNames(String dbName) {
        return getDatastore(dbName).getDB().getCollectionNames();
    }

    /**
     * 获取到指定集合
     * 
     * @param dbName
     * @param collectionName
     * @return
     */
    protected DBCollection getCollection(String dbName, String collectionName) {
        return getDatastore(dbName).getDB().getCollection(collectionName);
    }


    /**
     * 获取实体信息
     * 
     * @param c
     * @param dbName
     * @param fieldName
     * @param fieldValue
     * @return
     */
    protected <T> T getEntity(Class<T> c, String dbName, String fieldName, String fieldValue,
            ReadPreference readPreference) {
        Datastore datastore = getDatastore(dbName);

        datastore.getMongo().setReadPreference(readPreference);

        return datastore.find(c).field(fieldName).equal(fieldValue).get();
    }

    /**
     * 插入文档
     * 
     * @param dbName
     * @param entity
     */
    protected void insertEntiy(String dbName, Object entity) {
        Datastore datastore = getDatastore(dbName);
        datastore.save(entity);
    }

    /**
     * 插入文档
     * 
     * @param dbName
     * @param collectionName
     * @param entity
     */
    protected void insertEntiy(String dbName, String collectionName, Object entity) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<DBObject> collection = database.getCollection(collectionName, DBObject.class);

        DBObject dbObject = morphia.toDBObject(entity);

        collection.insertOne(dbObject);
    }

    /**
     * 忽略重复键异常
     * 
     * @param dbName
     * @param collectionName
     * @param entity
     */
    protected boolean insertEntityIngoreDuplicateKey(String dbName, String collectionName, Object entity) {
        boolean success = false;
        try {
            insertEntiy(dbName, collectionName, entity);
            success = true;
        } catch (RuntimeException e) {
            if (isDuplicateKeyException(e)) {
                success = true;
            } else {
                throw new RuntimeException(e);
            }
        }
        return success;
    }




    /**
     * 更新文档
     * 
     * @param dbName
     * @param collectionName
     * @param res
     */
    protected UpdateResult updateOne(String dbName, String collectionName, Bson doc, Bson res) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<DBObject> collection = database.getCollection(collectionName, DBObject.class);


        return collection.updateOne(doc, res);
    }


    /**
     * 删除文档
     * 
     * @param dbName
     * @param collectionName
     * @param filter
     */
    protected DeleteResult deleteOne(String dbName, String collectionName, Bson filter) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<DBObject> collection = database.getCollection(collectionName, DBObject.class);


        return collection.deleteOne(filter);
    }


    /**
     * 创建查询
     * 
     * @param type
     * @param dbName
     * @param collectionName
     * @return
     */
    protected <T> Query<T> newQuery(final Class<T> type, String dbName, String collectionName,
            ReadPreference readPreference) {
        Datastore datastore = getDatastore(dbName);
        datastore.getMongo().setReadPreference(readPreference);

        DBCollection collection = datastore.getDB().getCollection(collectionName);
        QueryFactory queryFactory = datastore.getQueryFactory();

        return queryFactory.createQuery(datastore, collection, type);
    }


    protected <T> MorphiaQuery<T> newQuery(EntityPath<T> type, String dbName) {
        return newQuery(type, dbName, ReadPreference.secondaryPreferred());
    }

    protected <T> MorphiaQuery<T> newQuery(EntityPath<T> type, String dbName, ReadPreference readPreference) {
        MorphiaQuery morphiaQuery = new MorphiaQuery<>(morphia, getDatastore(dbName), type);
        morphiaQuery.setReadPreference(readPreference);
        return morphiaQuery;
    }

    protected <T> Page<T> pager(MorphiaQuery<T> query, Pageable pageable){

        return pager(query,pageable,null);
    }

    protected <T> Page<T> pager(MorphiaQuery<T> query, Pageable pageable,OrderSpecifier orderSpecifier){
        long count = query.fetchCount();
        Page<T> page;

        if(count > 0){
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
            if(orderSpecifier != null){
                query.orderBy(orderSpecifier);
            }
            page = new PageImpl<T>(query.fetch(),pageable,count);
        }else {
            page = new PageImpl<T>(new ArrayList<>(),pageable,count);
        }
        return page;
    }

    protected AggregationPipeline createPipeline(String dbName,Class clazz){
        CustomDatastoreImpl datastore = (CustomDatastoreImpl)getDatastore(dbName);
        return datastore.createAggregation(clazz);
    }

}
