package com.github.skywaterxxs.configx.server.business;

import com.github.skywaterxxs.configx.remoting.ServerProcessor;
import io.netty.channel.Channel;

import java.util.concurrent.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.server.business.ServerProcessorImpl</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/27 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ServerProcessorImpl implements ServerProcessor {

    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    // creating the ThreadPoolExecutor
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            threadFactory);

    @Override
    public Executor produceExecutor() {
        return executorPool;
    }

    @Override
    public void execute(Object message, Channel channel, long dispatchTime) {

        System.out.println(message);

        channel.writeAndFlush("shoudaole");

    }
}
