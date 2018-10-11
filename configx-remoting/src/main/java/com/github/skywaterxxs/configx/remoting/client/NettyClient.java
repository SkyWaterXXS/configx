package com.github.skywaterxxs.configx.remoting.client;

import com.github.skywaterxxs.configx.remoting.codec.NettyProtocolDecoder;
import com.github.skywaterxxs.configx.remoting.codec.NettyProtocolEncoder;
import com.github.skywaterxxs.configx.remoting.server.NettySharedHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.client.NettyClient</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyClient {

    private final Map<Channel, NettyClient> channel2Client = new ConcurrentHashMap<Channel, NettyClient>();


    private RemotingURL url;

    private Channel channel;

    public RemotingURL getUrl() {
        return url;
    }

    public Channel getChannel() {
        return channel;
    }

    public NettyClient (){
    }

    protected NettyClient (RemotingURL url,Channel channel){
        this.url=url;
        this.channel=channel;
    }

    public NettyClient createClient(final RemotingURL url) throws Exception {
        final Bootstrap bootstrap = new Bootstrap();
        NettyClientHandler handler = new NettyClientHandler();
        bootstrap.group(NettySharedHolder.workerGroup)//
                .option(ChannelOption.TCP_NODELAY, true)//
                .option(ChannelOption.SO_REUSEADDR, true)//
                // .option(ChannelOption.SO_KEEPALIVE, true)//
                .option(ChannelOption.ALLOCATOR, NettySharedHolder.byteBufAllocator)//
                .channel(NioSocketChannel.class)//
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast("decoder", new NettyProtocolDecoder())
                                .addLast("encoder", new NettyProtocolEncoder())
                                .addLast("handler", handler);
                    }
                });
//        int connectTimeout = url.getParameter(TRConstants.CONNECT_TIMEOUT_KEY, 4000);
        int connectTimeout=1000;
//        if (connectTimeout < 1000) {
//            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
//        } else {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
//        }

        String targetIP = url.getHost();
        int targetPort = url.getPort();
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(targetIP, targetPort));
        if (future.awaitUninterruptibly(connectTimeout) && future.isSuccess() && future.channel().isActive()) {
            Channel channel = future.channel();
            NettyClient client = new NettyClient(url, channel);
            channel2Client.put(channel, client);
            return client;
        } else {
            future.cancel(true);
            future.channel().close();
//            LoggerInit.LOGGER_REMOTING.warn("[remoting] failure to connect:" + targetIP);
            throw new Exception("s");
        }
    }
}
