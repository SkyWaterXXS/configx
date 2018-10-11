package com.github.skywaterxxs.configx.client.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.client.chat.ChatClientChannelHandler</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/29 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChatClientChannelHandler extends ChannelDuplexHandler {

    /**
     * 接收server端的消息，并打印出来
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);

        System.out.println("Server said:" + new String(result1));
        Thread.sleep(10000);
        result.release();
    }

    /**
     * 连接成功后，向server发送消息
      */

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        String msg = "Are you ok?";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }

}
