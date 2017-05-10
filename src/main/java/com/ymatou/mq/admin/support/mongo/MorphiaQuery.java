/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.support.mongo;

import com.google.common.base.Function;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.querydsl.core.types.EntityPath;
import com.querydsl.mongodb.AbstractMongodbQuery;
import com.querydsl.mongodb.morphia.MorphiaSerializer;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.cache.DefaultEntityCache;
import org.mongodb.morphia.mapping.cache.EntityCache;

/**
 * @author luoshiqian 2017/3/29 13:18
 */
public class MorphiaQuery<K> extends AbstractMongodbQuery<K, MorphiaQuery<K>> {

    private final EntityCache cache;

    private final Datastore datastore;

    public MorphiaQuery(Morphia morphia, Datastore datastore, EntityPath<K> entityPath) {
        this(morphia, datastore, new DefaultEntityCache(), entityPath);
    }

    public MorphiaQuery(Morphia morphia, Datastore datastore, Class<? extends K> entityType) {
        this(morphia, datastore, new DefaultEntityCache(), entityType);
    }

    public MorphiaQuery(Morphia morphia, Datastore datastore,
            EntityCache cache, EntityPath<K> entityPath) {
        this(morphia, datastore, cache, entityPath.getType());
    }

    public MorphiaQuery(final Morphia morphia, final Datastore datastore,
            final EntityCache cache, final Class<? extends K> entityType) {
        super(MongoThreadContext.getDbCollection(datastore, entityType), new Function<DBObject, K>() {
            @Override
            public K apply(DBObject dbObject) {
                return morphia.fromDBObject(datastore, entityType, dbObject, cache);
            }
        }, new MorphiaSerializer(morphia));
        this.datastore = datastore;
        this.cache = cache;
    }


    @Override
    protected DBCursor createCursor() {
        cache.flush();
        return super.createCursor();
    }

    @Override
    public DBCollection getCollection(Class<?> type) {
        return MongoThreadContext.getDbCollection(datastore, type);
    }

}
