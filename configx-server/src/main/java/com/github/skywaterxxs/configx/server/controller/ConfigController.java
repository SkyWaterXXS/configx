package com.github.skywaterxxs.configx.server.controller;


import com.github.skywaterxxs.configx.server.business.ConfigBusiness;
import com.github.skywaterxxs.configx.server.chat.ChatServer;
import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Hashtable;
import java.util.List;
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
        Map<String, String> configs = new Hashtable<>();
        configs.put("name", "xxs");

        List<Channel> channels = ChatServer.nettyServer.listChannels();

        for (Channel channel : channels) {
            channel.writeAndFlush("sshkjk");
        }
        return configs;
    }

}