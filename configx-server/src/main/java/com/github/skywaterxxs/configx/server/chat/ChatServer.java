package com.github.skywaterxxs.configx.server.chat;

import com.github.skywaterxxs.configx.remoting.server.NettyServer;
import com.github.skywaterxxs.configx.server.business.ServerProcessorImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.server.chat.ChatServer</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/29 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class ChatServer {

    public static   NettyServer nettyServer;

    @PostConstruct
    public void init() {

        Thread a = new Thread(() -> {
            NettyServer server = new NettyServer("127.0.0.1",new ServerProcessorImpl());
            try {
                server.start(7000);
                nettyServer=server;
                } catch (Exception e) {
                e.printStackTrace();
            }

        });
        a.start();
        System.out.println("ChatServer_start");
    }
}
