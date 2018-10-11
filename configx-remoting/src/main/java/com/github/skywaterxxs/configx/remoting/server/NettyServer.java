package com.github.skywaterxxs.configx.remoting.server;

import com.github.skywaterxxs.configx.remoting.Connection;
import com.github.skywaterxxs.configx.remoting.ProtocolFactory;
import com.github.skywaterxxs.configx.remoting.ResponseStatus;
import com.github.skywaterxxs.configx.remoting.RpcResponse;
import com.github.skywaterxxs.configx.remoting.codec.NettyProtocolDecoder;
import com.github.skywaterxxs.configx.remoting.codec.NettyProtocolEncoder;
import com.github.skywaterxxs.configx.remoting.util.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.internal.SystemPropertyUtil;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.skywaterxxs.configx.remoting.HSFThreadNameSpace.HSF_NETTY_BOSS;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.server.NettyServer</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyServer implements Server{


//    private static final Logger LOGGER = LoggerInit.LOGGER_REMOTING;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(0, new NamedThreadFactory(
            HSF_NETTY_BOSS));
    private final EventLoopGroup workerGroup = NettySharedHolder.workerGroup;

    private final NettyServerHandler serverHandler = new NettyServerHandler();
//    private final RpcRequestProcessor rpcProcessor;

    private final AtomicBoolean startFlag = new AtomicBoolean(false);
    private final AtomicBoolean startHttpFlag = new AtomicBoolean(false);
    private final String bindHost;

    public NettyServer(final String bindHost) {
//        this.rpcProcessor = rpcProcessor;
//        ProtocolFactory.instance.initServerSide(rpcProcessor);
        this.bindHost = bindHost;

//        NettySharedHolder.timer.newTimeout(new ScanAllClientRunnerServerSide(serverHandler), 59, TimeUnit.SECONDS);
    }

    public void start(int listenPort) throws Exception {
        if (!startFlag.compareAndSet(false, true)) {
            return;
        }
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 可配置的几个
        bootstrap.group(bossGroup, workerGroup)//
                .channel(NioServerSocketChannel.class)//
                // .option(ChannelOption.SO_BACKLOG,
                // SystemPropertyUtil.getInt("hsf.backlog", 100000))//
                .option(ChannelOption.ALLOCATOR, NettySharedHolder.byteBufAllocator)//
                .childOption(ChannelOption.ALLOCATOR, NettySharedHolder.byteBufAllocator)//
                .childOption(ChannelOption.TCP_NODELAY, true)//
                .childOption(ChannelOption.SO_REUSEADDR, true)
                // .childOption(ChannelOption.SO_KEEPALIVE, true)//
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new NettyProtocolDecoder())//
                                .addLast("encoder", new NettyProtocolEncoder())//
                                .addLast("handler", serverHandler);
                    }
                });

        long tryBind = 3;
        while (tryBind > 0) {
            ChannelFuture cf = bootstrap.bind(new InetSocketAddress(bindHost, listenPort));
            cf.await();
            if (cf.isSuccess()) {
                System.out.println("server online bindHost="+bindHost+",listenPort="+listenPort);
//                LOGGER.warn("Server started,listen at: " + listenPort);
                return;
            } else {
                tryBind--;
                if (tryBind <= 0) {
//                    LOGGER.warn("After 3 failed attempts to start server at port : " + listenPort
//                            + ", we are shutting down the vm");
                    System.exit(1);
                } else {
//                    LOGGER.warn("Failed to start server at port : " + listenPort + ", Sleep 3s and try again",
//                            cf.cause());
                    Thread.sleep(3000);
                }
            }
        }
    }

    public List<Channel> listChannels() {
        return serverHandler.getChannels();
    }

    public void stop() throws Exception {
//        LOGGER.warn("Server stop!");
        this.startFlag.set(false);
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    @Override
    public void signalClosingServer() {
//        RpcResponse msg = new RpcResponse(-1, Codecs.HESSIAN2_CODEC, new byte[1]);
//        msg.setStatus(ResponseStatus.SERVER_CLOSING);
//        for (Channel channel : serverHandler.getChannels()) {
//            channel.writeAndFlush(msg);
//        }
    }

//    @Override
//    public void startHttp(int listenPort) throws Exception {
//        if (!startHttpFlag.compareAndSet(false, true)) {
//            return;
//        }
//
//        ServerBootstrap bootstrap = new ServerBootstrap();
//        bootstrap.group(bossGroup, workerGroup)//
//                .channel(NioServerSocketChannel.class)//
//                .option(ChannelOption.SO_BACKLOG, SystemPropertyUtil.getInt("hsf.backlog.http", 100000))//
//                .option(ChannelOption.ALLOCATOR, NettySharedHolder.byteBufAllocator)//
//                .childOption(ChannelOption.ALLOCATOR, NettySharedHolder.byteBufAllocator)//
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel channel) throws Exception {
//                        ChannelPipeline pipeline = channel.pipeline();
//                        // Uncomment the following line if you want HTTPS
//                        // SSLEngine engine =
//                        // SecureChatSslContextFactory.getServerContext().createSSLEngine();
//                        // engine.setUseClientMode(false);
//                        // p.addLast("ssl", new SslHandler(engine));
//
//                        pipeline.addLast("decoder", new HttpRequestDecoder());
//                        // comment the following line if you don't want to
//                        // handle HttpChunks.
//                        pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
//                        pipeline.addLast("encoder", new HttpResponseEncoder());
//                        // Remove the following line if you don't want automatic
//                        // content compression.
//                        // pipeline.addLast("deflater", new
//                        // HttpContentCompressor());
//                        pipeline.addLast("handler", new NettyServerHttpHandler(rpcProcessor));
//                    }
//
//                });
//
//        long tryBind = 3;
//        while (tryBind > 0) {
//            ChannelFuture cf = bootstrap.bind(new InetSocketAddress(bindHost, listenPort));
//            cf.await();
//            if (cf.isSuccess()) {
//                LOGGER.warn("Server started http transport, while listen at: " + listenPort);
//                return;
//            } else {
//                tryBind--;
//                if (tryBind <= 0) {
//                    LOGGER.warn("After 3 failed attempts to start http transport at port : " + listenPort
//                            + ", we are shutting down the vm");
//                    System.exit(1);
//                } else {
//                    LOGGER.warn("Failed to start http transport at port : " + listenPort, cf.cause());
//                    Thread.sleep(3000);
//                }
//            }
//        }
//    }

    @Override
    public void closeConnections() {
        for (Connection connection : serverHandler.getConnections()) {
            try {
                connection.close();
            } catch (Exception e) {
//                LOGGER.error("[Connection Close]", e.getMessage());
            }
        }

    }

}
