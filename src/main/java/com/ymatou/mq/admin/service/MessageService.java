/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.service;

import static com.ymatou.mq.admin.model.MessageDetailStatusEnums.findByCode;
import static com.ymatou.mq.admin.service.MessageConfigService.callbackConfigMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ymatou.mq.admin.model.enums.CompensateStatusEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.ymatou.mq.admin.model.*;
import com.ymatou.mq.admin.repository.MessageCompensateRepository;
import com.ymatou.mq.admin.repository.MessageDispatchDetailRepository;
import com.ymatou.mq.admin.repository.MessageRepository;

/**
 * @author luoshiqian 2017/4/5 16:28
 */
@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageDispatchDetailRepository dispatchDetailRepository;
    @Autowired
    private MessageCompensateRepository compensateRepository;


    public Object findMessageList(MessageCondition messageCondition, Pageable pageable) {
        Page<Message> messagePage = messageRepository.findByCondition(messageCondition, pageable);
        if (!CollectionUtils.isEmpty(messagePage.getContent())) {

            List<MessageVo> messageVoList = Lists.newArrayList();

            List<String> msgIdList = messagePage.getContent().stream().map(Message::getId).collect(Collectors.toList());

            List<MessageDetailGroup> detailGroups = dispatchDetailRepository
                    .findStatusByMsgIds(messageCondition.getAppId(), messageCondition.getQueueCode(), msgIdList);

            messagePage.getContent().forEach(message -> {
                MessageVo messageVo = new MessageVo();
                BeanUtils.copyProperties(message, messageVo);

                detailGroups.stream().filter(group -> group.getMsgId().equals(message.getId())).forEach(group -> {
                    switch (findByCode(group.getStatus())) {
                        case INIT:
                            messageVo.setInitCount(group.getCount());
                            return;
                        case SUCCESS:
                            messageVo.setSuccessCount(group.getCount());
                            return;
                        case FAIL:
                            messageVo.setFailCount(group.getCount());
                            return;
                        case COMPENSATE:
                            messageVo.setCompensateCount(group.getCount());
                            return;
                    }
                });

                messageVoList.add(messageVo);
            });
            return new PageImpl<>(messageVoList,pageable,messagePage.getTotalElements());
        }

        return messagePage;
    }

    public Object dispatchDetail(String appId, String queueCode, String msgId) {
        List<MessageDispatchDetail> dispatchDetailList = dispatchDetailRepository.findByMsgId(appId, queueCode, msgId);

        // 关联查询
        assembleDispatchDetail(dispatchDetailList,appId,queueCode);

        return dispatchDetailList;
    }

    private void assembleDispatchDetail(List<MessageDispatchDetail> list,String appId,String queueCode){
        if (!CollectionUtils.isEmpty(list)) {

            List<String> idList = list.stream().map(MessageDispatchDetail::getId).collect(Collectors.toList());
            List<MessageCompensate> compensateList = compensateRepository.findByQueueCodeAndIdList(appId, queueCode,idList);

            for(MessageDispatchDetail dispatchDetail : list){
                Optional<MessageCompensate> messageCompensateOptional = compensateList.stream()
                        .filter(messageCompensate -> messageCompensate.getId().equals(dispatchDetail.getId()))
                        .findFirst();
                dispatchDetail.setCallNum(1);
                if(messageCompensateOptional.isPresent()){
                    MessageCompensate compensate = messageCompensateOptional.get();
                    dispatchDetail.setCallNum(compensate.getCompensateNum() + 1);

                    if(compensate.getStatus() == CompensateStatusEnum.COMPENSATE.getCode()){
                        dispatchDetail.setNextTime(compensate.getNextTime());
                        dispatchDetail.setLastFrom(compensate.getLastFrom());
                        dispatchDetail.setDealIp(compensate.getDealIp());
                        dispatchDetail.setLastTime(compensate.getLastTime());
                        dispatchDetail.setLastResp(compensate.getLastResp());
                    }
                }
                CallbackConfig callbackConfig = callbackConfigMap.get(dispatchDetail.getConsumerId());
                if(callbackConfig != null){
                    dispatchDetail.setCallbackUrl(callbackConfig.getUrl());
                }
            }
        }
    }

    public Object dispatchDetailList(MessageCondition messageCondition,Pageable pageable) {

        Page<MessageDispatchDetail> dispatchDetailPage = dispatchDetailRepository.findByCondition(messageCondition,pageable);

        if (!CollectionUtils.isEmpty(dispatchDetailPage.getContent())) {
            assembleDispatchDetail(dispatchDetailPage.getContent(),messageCondition.getAppId(),messageCondition.getQueueCode());
        }

        return dispatchDetailPage;
    }
}
