package com.github.skywaterxxs.configx.server.controller;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.server.business.ConfigBusiness;
import com.github.skywaterxxs.configx.server.chat.ChatClientChannelStore;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Hashtable;
import java.util.Iterator;
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

        try{
            Map<String, ChannelHandlerContext> map = ChatClientChannelStore.getChannels();
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                ChannelHandlerContext ctx = map.get(key);
                // 向客户端发送消息
                String response = "sdsdssdsd";
                // 在当前场景下，发送的数据必须转换成ByteBuf数组
                ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
                encoded.writeBytes(response.getBytes());
                ctx.write(encoded);
                ctx.flush();            }
        }catch(Exception e){

            e.printStackTrace();
        }


        return "true";
    }

}