/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.contoller;

import com.ymatou.mq.admin.util.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ymatou.mq.admin.model.Permission;
import com.ymatou.mq.admin.repository.PermissionRepository;
import com.ymatou.mq.admin.util.WapperUtil;

import java.util.Date;

/**
 * @author luoshiqian 2017/4/11 16:12
 */
@RequestMapping("/permission")
@RestController
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @RequestMapping("/list")
    public Object list(String loginName, Pageable pageable) {

        return WapperUtil.success(permissionRepository.findByPage(loginName, pageable));
    }

    @RequestMapping("/save")
    @ResponseBody
    public Object saveApp(Permission permission,String permissionId, @RequestParam(name = "oper") String oper) {
        if (oper.equals("add") || oper.equals("edit")) {
            permissionRepository.save(permission,permissionId);
        } else if (oper.equals("del")) {
            permissionRepository.deleteById(new ObjectId(permissionId));
        }
        return WapperUtil.success("保存成功!");
    }

}
