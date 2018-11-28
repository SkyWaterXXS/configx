package com.github.skywaterxxs.configx.remoting.client;

import com.github.skywaterxxs.configx.remoting.RemotingRuntimeInfoHolder;
import io.netty.channel.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.client.NettyClientHandler</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyClientHandler extends ChannelDuplexHandler {
    private static final String ERROR_TO_WRITE_CHANNEL = "Send request error when write to channel";

//    private final Map<Long, BaseRequestWrapperInterface> mappers = Maps.newHashMap();

    ClientProcessor clientProcessor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            clientProcessor.processor(ctx.channel(),(String) msg);
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.net.ConnectException) {
            return;
        }
//        LOGGER.warn(LogConstants.PREFIX_IMPORTANT + "catch some exception:" + ctx.channel(), cause);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        RemotingRuntimeInfoHolder.getInstance().increaseCountConnectionsAsClient();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
////        LOGGER.info(LogConstants.PREFIX_IMPORTANT + "connection closed: " + factory.getCientByChannel(ctx.channel()));
//        RemotingRuntimeInfoHolder.getInstance().decreaseCountConnectionsAsClient();
//        removeAllRequestCallBackWhenClose();
////        factory.remove(ctx.channel());
        super.channelInactive(ctx);
    }


}
