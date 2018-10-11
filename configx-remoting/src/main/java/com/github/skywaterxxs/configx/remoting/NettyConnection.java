package com.github.skywaterxxs.configx.remoting;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.MessageFormat;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.NettyConnection</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyConnection implements Connection {
//    private static final Logger LOGGER = LoggerInit.LOGGER_REMOTING;
    private final Channel channel;
    private final String peerIP;

    private volatile long lastReadTime = System.currentTimeMillis();

    public NettyConnection(final Channel channel) {
        this.channel = channel;
        this.peerIP = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    public NettyConnection() {
        this.channel = null;
        this.peerIP = null;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public SocketAddress getLocalAddress() {
        return channel.localAddress();
    }

    @Override
    public String getPeerIP() {
        return peerIP;
    }

    @Override
    public void writeToChannel(final BaseResponse response) {
        if (response != null) {
            // if (channel.isWritable()) {
            ChannelFuture wf = channel.writeAndFlush(response);
            wf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
//                        LOGGER.error("", LogConstants.PREFIX_IMPORTANT + "server write response error,request id is: "
//                                + response.getRequestID() + ", channel:" + NettyConnection.this.getPeerIP()
//                                + ((null != future.cause())? ", cause:" + future.cause(): ""));
                        // need response or not under this condition?
                        // this.sendErrorResponse(NettyConnection.this, request);
                        if (!channel.isActive()) {
                            channel.close();
                        }
                    }
                }
            });
        }
        // }
    }

    @Override
    public String toString() {
        return MessageFormat.format("L:{0},R:{1}", this.getLocalAddress(), this.getRemoteAddress());
    }

    @Override
    public void refreshLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Override
    public long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Override
    public void close() {
        this.channel.close();
    }
}
