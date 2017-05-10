
/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.contoller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymatou.mq.admin.model.MessageCondition;
import com.ymatou.mq.admin.repository.MessageRepository;
import com.ymatou.mq.admin.service.MessageService;
import com.ymatou.mq.admin.util.WapperUtil;


@Controller
@RequestMapping("/message")
public class MessageController {

    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;


    @RequestMapping("/list")
    @ResponseBody
    public Object list(@Valid MessageCondition messageCondition, Pageable pageable) {

        return WapperUtil.success(messageService.findMessageList(messageCondition,pageable));
    }


    @RequestMapping("/detail/{appId}/{queueCode}/{id}")
    @ResponseBody
    public Object detail(@PathVariable("appId") String appId, @PathVariable("queueCode") String queueCode,
                         @PathVariable("id") String id) {
        return WapperUtil.success(messageService.dispatchDetail(appId, queueCode, id));
    }

    @RequestMapping("/detail/list")
    @ResponseBody
    public Object detail(@Valid MessageCondition messageCondition, Pageable pageable) {
        return WapperUtil.success(messageService.dispatchDetailList(messageCondition,pageable));
    }

}
