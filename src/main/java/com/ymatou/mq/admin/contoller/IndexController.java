/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.mq.admin.contoller;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ymatou.mq.admin.model.User;
import com.ymatou.mq.admin.util.CurrentUserUtil;
import com.ymatou.mq.admin.util.WapperUtil;


@Controller
@RequestMapping("")
public class IndexController {

    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/")
    public String index() {

        return "forward:/login.html";
    }

    @RequestMapping("/getCurrentUser")
    @ResponseBody
    public Object getCurrentUser() {
        User user = CurrentUserUtil.getCurrentUser();
        if (user == null) {
            return WapperUtil.error("未登录!");
        }
        user.setPassword("");
        return WapperUtil.success(CurrentUserUtil.getCurrentUser());
    }


    // http://www.guriddo.net/demo/bootstrap/
    @RequestMapping("/data.json")
    @ResponseBody
    public String gridData() throws Exception {
        return new String(Files.readAllBytes(
                Paths.get(ConfigController.class.getResource("/data.json").toURI())), Charset.forName("UTF-8"));
    }
}
