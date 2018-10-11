package com.github.skywaterxxs.configx.test;

import com.github.skywaterxxs.configx.client.chat.ChatClient;
import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import com.github.skywaterxxs.configx.remoting.client.NettyClient;
import com.github.skywaterxxs.configx.remoting.client.RemotingURL;
import io.netty.buffer.ByteBuf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xuxiaoshuo 2018/4/10
 */
@Controller
@RequestMapping("/helloworld")
public class HelloWorldController {

    @Autowired
    ConfigXService configXService;

    //http://127.0.0.1:8080/helloworld/testDataSource?id=1
    @RequestMapping("/testDataSource")
    @ResponseBody
    public void testDataSource(int id) {

//        // 向客户端发送消息
//        String response = "sdfsdafdsafds";
//        // 在当前场景下，发送的数据必须转换成ByteBuf数组
//        ByteBuf encoded = ChatClient.channel.alloc().buffer(4 * response.length());
//        encoded.writeBytes(response.getBytes());
//        ChatClient.channel.writeAndFlush(encoded);
//
//        ChatClient.channel.read();


        System.out.println(ConfigXStore.clientStore.size());


        NettyClient nettyClient= ConfigXStore.clientStore.get("127.0.0.1");


        nettyClient.getChannel().writeAndFlush("23232");

        System.out.println(configXService.getName());

    }
}