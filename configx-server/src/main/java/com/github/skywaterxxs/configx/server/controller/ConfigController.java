package com.github.skywaterxxs.configx.server.controller;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.server.business.ConfigBusiness;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author xuxiaoshuo 2018/4/10
 */
@Controller
@RequestMapping("/config")
public class ConfigController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);


    @Resource
    private ConfigBusiness configBusiness;

    //http://127.0.0.1:5210/config/init/v1
    @RequestMapping("/init/v1")
    @ResponseBody
    public Object init() {
        Map<String,String> configs=new Hashtable<>();
        configs.put("name","xxs");

        return configs;
    }

    //http://127.0.0.1:5210/config/update/v1
    @RequestMapping("/update/v1")
    @ResponseBody
    public Object update() {
        return configBusiness.update();
    }

}