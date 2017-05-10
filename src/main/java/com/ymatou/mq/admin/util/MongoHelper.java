/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;


import com.ymatou.mq.admin.model.MessageDispatchDetail;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author luoshiqian 2017/3/27 15:50
 */
public class MongoHelper {

    private static final String DB_PREFIX = "JMQ_V2_Message_";
    private static final String MESSAGE_COLLECTION_PREFIX = "Message_";


    /**
     * 获取库名
     * 
     * @param appId
     * @param id
     * @return
     */
    public static String getDbName(String appId, String id) {
        String dbName = DB_PREFIX + appId + "_"
                + getDateFromMongoId(id, Constants.DATE_FORMAT_YYYY_MM);
        return dbName;
    }

    public static String getDbName(String appId,Date date){
        String dbName = DB_PREFIX + appId + "_" + DateFormatUtils.format(date, Constants.DATE_FORMAT_YYYY_MM);

        return dbName;
    }

    /**
     * 获取消息补单表名
     * @param queueCode
     * @return
     */
    public static String getMessageCompensateCollectionName(String appId,String queueCode) {
        return appId + "_" + queueCode;
    }
    /**
     * 获取消息表名
     * 
     * @param queueCode
     * @return
     */
    public static String getMessageCollectionName(String queueCode) {
        return MESSAGE_COLLECTION_PREFIX + queueCode;
    }


    /**
     * 获取消息分发表 表名
     * 
     * @param queueCode
     * @return
     */
    public static String getMessageDetailCollectionName(String queueCode) {
        return MESSAGE_COLLECTION_PREFIX + queueCode + "_detail";
    }



    /**
     * 从mongo id中获取所在时间格式
     * 
     * @param id
     * @param pattern
     * @return
     */
    public static String getDateFromMongoId(String id, String pattern) {
        return DateFormatUtils.format(new ObjectId(id).getDate(), pattern);
    }




}
