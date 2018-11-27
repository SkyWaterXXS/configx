package com.github.skywaterxxs.configx.test;

import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import com.github.skywaterxxs.configx.remoting.client.NettyClient;
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

    //http://127.0.0.1:8080/helloworld/testDataSource?msg=1
    @RequestMapping("/testDataSource")
    @ResponseBody
    public void testDataSource(String  msg) {

        NettyClient nettyClient= ConfigXStore.clientStore.get("127.0.0.1");


        nettyClient.getChannel().writeAndFlush(msg);

//        System.out.println(configXService.getName());

    }
}