package com.github.skywaterxxs.configx.remoting.server;

import com.github.skywaterxxs.configx.remoting.*;
import io.netty.channel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.server.NettyServerHandler</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //    private final static Logger LOGGER = LoggerInit.LOGGER_REMOTING;
    private final ConcurrentMap<Channel, NettyConnection> channels = new ConcurrentHashMap<Channel, NettyConnection>();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.net.ConnectException || cause instanceof IOException) {
//            LOGGER.warn("", ctx.channel().remoteAddress() + " may be closed by client");
            return;
        }

        // only log
//        LOGGER.warn("", LogConstants.PREFIX_IMPORTANT + " catch some exception:", cause);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerProcessor serverProcessor = ProcessorFactory.getProcessor(msg);

        if (serverProcessor == null || serverProcessor.produceExecutor() == null) {
            return;
        }

        Executor executor = serverProcessor.produceExecutor();
        try {
            executor.execute(new HandlerRunnable(ctx.channel(), msg, serverProcessor));

        } catch (Throwable t) {
//                LOGGER.error("", "Local HSF thread pool is full." + t.getMessage());
//        BaseResponse responseWrapper = request.createErrorResponse("Provider's HSF thread pool is full.");
//        responseWrapper.setStatus(ResponseStatus.THREADPOOL_BUSY);
//        ctx.channel().writeToChannel(responseWrapper);
        }

    }

    public List<Channel> getChannels() {
        return new ArrayList<Channel>(channels.keySet());
    }

    public List<Connection> getConnections() {
        return new ArrayList<Connection>(channels.values());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RemotingRuntimeInfoHolder.getInstance().increaseCountConnectionsAsServer();
        Channel channel = ctx.channel();
        channels.putIfAbsent(channel, new NettyConnection(channel));
        super.channelActive(ctx);

        System.out.println("客户端接入" + ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        RemotingRuntimeInfoHolder.getInstance().decreaseCountConnectionsAsServer();
        ctx.channel().close();
        channels.remove(ctx.channel());
        super.channelInactive(ctx);
    }


    private static class HandlerRunnable implements Runnable {
        private final Channel connection;
        private final Object message;
        private final long dispatchTime;
        private final ServerProcessor serverProcessor;

        public HandlerRunnable(Channel conneciton, Object message, ServerProcessor serverProcessor) {
            this.connection = conneciton;
            this.message = message;
            this.dispatchTime = System.currentTimeMillis();
            this.serverProcessor = serverProcessor;
        }

        @Override
        public void run() {
//            EagleEye.requestSize(message.size());
            serverProcessor.execute(message, connection, dispatchTime);
        }

    }

}
