package com.github.skywaterxxs.configx.server.chat;

import com.github.skywaterxxs.configx.remoting.server.NettyServer;
import com.github.skywaterxxs.configx.remoting.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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


    @PostConstruct
    public void init() {

        Thread a = new Thread(() -> {
            Server server=new NettyServer("127.0.0.1");
            try {
                server.start(7000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        a.start();
        System.out.println("ChatServer_start");
    }

    public void start(int port) {
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                // 注册handler
                                ch.pipeline().addLast(new ChatServerChannelHandler());
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture f = null;

                f = b.bind(port).sync();


                f.channel().closeFuture().sync();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
