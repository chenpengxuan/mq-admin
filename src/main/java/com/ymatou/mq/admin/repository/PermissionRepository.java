/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.repository;

import java.util.Date;
import java.util.List;

import com.ymatou.mq.admin.util.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.ymatou.mq.admin.config.MongoClientConfig;
import com.ymatou.mq.admin.model.Permission;
import com.ymatou.mq.admin.model.QPermission;
import com.ymatou.mq.admin.support.MongoRepository;
import com.ymatou.mq.admin.support.mongo.MorphiaQuery;

/**
 * @author luoshiqian 2017/4/11 15:13
 */
@Component
public class PermissionRepository extends MongoRepository {

    @Autowired
    private MongoClientConfig mongoClientConfig;
    private MongoClient mongoClient;

    private static final String dbName = "MQ_Configuration_201609";

    @Override
    protected MongoClient getMongoClient() {
        return mongoClientConfig.getMongoClientIfNullSet(mongoClient, "configMongoClient");
    }

    public List<Permission> findByLoginName(String loginName) {
        return getDatastore(dbName).find(Permission.class).filter("loginName =", loginName).asList();
    }

    public Page<Permission> findByPage(String loginName, Pageable pageable) {
        QPermission qPermission = new QPermission("");

        MorphiaQuery query = newQuery(qPermission, dbName);

        if (StringUtils.isNotBlank(loginName)) {
            query.where(qPermission.loginName.contains(loginName));
        }

        return pager(query, pageable);
    }

    public void save(Permission permission,String permissionId) {
        if(StringUtils.isBlank(permissionId)){
            permission.setCreateTime(new Date());
            permission.setCreator(CurrentUserUtil.getCurrentUserName());
            insertEntiy(dbName, permission);
        }else {
            Datastore ds = getDatastore(dbName);
            UpdateOperations<Permission> ups = ds.createUpdateOperations(Permission.class);
            ups.set("action",permission.getAction());
            ups.set("enable",permission.getEnable());
            ups.set("updateTime",new Date());
            ups.set("updator",CurrentUserUtil.getCurrentUserName());

            ds.update(ds.createQuery(Permission.class).field("id").equal(new ObjectId(permissionId)),ups);
        }
    }

    public void deleteById(ObjectId id) {
        getDatastore(dbName).delete(Permission.class, id);
    }
}
