package com.github.skywaterxxs.configx.server.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.server.chat.ChatClientChannelStore</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/29 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChatClientChannelStore {


    private static Map<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();

    public static void addGatewayChannel(String id, ChannelHandlerContext socketChannel) {
        map.put(id, socketChannel);
    }

    public static Map<String, ChannelHandlerContext> getChannels() {
        return map;
    }

    public static ChannelHandlerContext getGatewayChannel(String id) {
        return map.get(id);
    }

    public static void removeGatewayChannel(String id) {
        map.remove(id);
    }

}
