
/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymatou.mq.admin.model.AppConfig;
import com.ymatou.mq.admin.model.CallbackCondition;
import com.ymatou.mq.admin.model.CallbackConfig;
import com.ymatou.mq.admin.model.QueueConfig;
import com.ymatou.mq.admin.repository.AppConfigRepository;
import com.ymatou.mq.admin.util.WapperUtil;


@Controller
@RequestMapping("/appConfig")
public class AppConfigController {

    private final static Logger logger = LoggerFactory.getLogger(AppConfigController.class);


    @Autowired
    private AppConfigRepository appConfigRepository;


    @RequestMapping("/list")
    @ResponseBody
    public Object list(String appId, String groupId, Pageable pageable) {

        return WapperUtil.success(appConfigRepository.findByPage(appId, groupId, pageable));
    }


    @RequestMapping("/detail/{appId}")
    @ResponseBody
    public Object detail(@PathVariable("appId") String appId) {

        return WapperUtil.success(appConfigRepository.getAppConfig(appId).getMessageCfgList());
    }

    @RequestMapping("/queue/{appId}/{queueCode}")
    @ResponseBody
    public Object queueDetail(@PathVariable("appId") String appId, @PathVariable("queueCode") String queueCode) {
        return WapperUtil.success(appConfigRepository.getAppConfig(appId).getMessageConfig(queueCode));
    }

    @RequestMapping("/callback/{appId}/{queueCode}/{callbackKey}")
    @ResponseBody
    public Object callbackDetail(@PathVariable("appId") String appId, @PathVariable("queueCode") String queueCode,@PathVariable("callbackKey") String callbackKey) {
        return WapperUtil.success(appConfigRepository.getAppConfig(appId).getMessageConfig(queueCode).getCallbackConfig(callbackKey));
    }


    @RequestMapping("/callbackList")
    @ResponseBody
    public Object callbackList(CallbackCondition callbackCondition, Pageable pageable) {
        return WapperUtil.success(appConfigRepository.findCallbackPage(callbackCondition, pageable));
    }


    @RequestMapping("/saveApp")
    @ResponseBody
    public Object saveApp(AppConfig appConfig, @RequestParam(name = "oper") String oper) {
        if (oper.equals("add") || oper.equals("edit")) {
            appConfigRepository.saveAppConfig(appConfig);
        } else if (oper.equals("del")) {
            appConfigRepository.deleteById(appConfig.getAppId());
        }
        return WapperUtil.success("保存成功!");
    }

    @RequestMapping("/queue/save/{appId}")
    @ResponseBody
    public Object saveQueue(@PathVariable("appId") String appId, QueueConfig queueConfig) {
        appConfigRepository.saveQueueConfig(appId, queueConfig);
        return WapperUtil.success("保存成功!");
    }

    @RequestMapping("/callback/save/{appId}/{queueCode}")
    @ResponseBody
    public Object saveCallback(@PathVariable("appId") String appId, @PathVariable("queueCode") String queueCode,
            CallbackConfig callbackConfig) {

        appConfigRepository.saveCallbackConfig(appId, queueCode, callbackConfig);
        return WapperUtil.success("保存成功!");
    }

}
