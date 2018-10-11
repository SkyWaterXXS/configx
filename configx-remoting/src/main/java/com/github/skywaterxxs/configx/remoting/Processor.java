package com.github.skywaterxxs.configx.remoting;

import io.netty.channel.Channel;

import java.util.concurrent.Executor;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.Processor</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/3 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Processor {

    Executor produceExecutor();

    void execute(Object message, Channel channel, long dispatchTime);
}
