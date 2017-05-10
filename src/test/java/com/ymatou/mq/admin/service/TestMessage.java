/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymatou.mq.admin.BaseTest;
import com.ymatou.mq.admin.model.Message;
import com.ymatou.mq.admin.repository.MessageRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author luoshiqian 2017/4/17 16:47
 */
public class TestMessage extends BaseTest {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void test()throws Exception{

        Message message = messageRepository.getById("test_javaV2","test2","58f4788a3183410cfc6607f0");

        ObjectMapper mapper = new ObjectMapper();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        mapper.setDateFormat(dateFormat);
//        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println(JSON.toJSONStringWithDateFormat(message,"yyyy-MM-dd HH:mm:ss"));
        System.out.println(mapper.writeValueAsString(message));

    }

}
