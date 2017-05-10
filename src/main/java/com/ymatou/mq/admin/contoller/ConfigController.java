/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.contoller;

import java.util.stream.Collectors;

import com.ymatou.mq.admin.model.QueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymatou.mq.admin.model.AppConfig;
import com.ymatou.mq.admin.repository.AppConfigRepository;
import com.ymatou.mq.admin.util.WapperUtil;

import static java.util.stream.Collectors.toList;


@Controller
@RequestMapping("/config")
public class ConfigController {

    private final static Logger logger = LoggerFactory.getLogger(ConfigController.class);


    @Autowired
    private AppConfigRepository appConfigRepository;

    @RequestMapping("/getAllApp")
    @ResponseBody
    public Object getAllApp() {
        return WapperUtil.success(appConfigRepository.getAllAppConfig().stream().map(AppConfig::getAppId).collect(toList()));
    }


    // http://www.guriddo.net/demo/bootstrap/
    @RequestMapping("/getAllQueueByAppId")
    @ResponseBody
    public Object getAllQueueByAppId(String appId) {
        AppConfig appConfig = appConfigRepository.getAppConfig(appId);

        return WapperUtil.success(appConfig.getMessageCfgList().stream().map(QueueConfig::getCode).collect(toList()));
    }
}
