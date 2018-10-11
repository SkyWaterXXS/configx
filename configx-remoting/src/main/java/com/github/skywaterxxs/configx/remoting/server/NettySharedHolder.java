package com.github.skywaterxxs.configx.remoting.server;

import com.github.skywaterxxs.configx.remoting.HSFThreadNameSpace;
import com.github.skywaterxxs.configx.remoting.util.NamedThreadFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.server.NettySharedHolder</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettySharedHolder {
    public static final NioEventLoopGroup workerGroup = new NioEventLoopGroup(SystemPropertyUtil.getInt(
            "hsf.ioworkers", Runtime.getRuntime().availableProcessors() * 2), new NamedThreadFactory(
            HSFThreadNameSpace.HSF_NETTY_WORKER));

    public static final Timer timer = new HashedWheelTimer(
            new NamedThreadFactory(HSFThreadNameSpace.HSF_REMOTING_TIMER));

    public static final ByteBufAllocator byteBufAllocator;

    static {
        workerGroup.setIoRatio(SystemPropertyUtil.getInt("hsf.ioratio", 100));

        // 默认不用pooled
        if (SystemPropertyUtil.getBoolean("hsf.bytebuf.pool", false)) {
            byteBufAllocator = PooledByteBufAllocator.DEFAULT;
        } else {
            byteBufAllocator = UnpooledByteBufAllocator.DEFAULT;
        }
    }
}
