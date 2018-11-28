package com.github.skywaterxxs.configx.remoting.client;


import io.netty.channel.Channel;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.client.ClientProcessor</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/27 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ClientProcessor {

    void processor(Channel channel, String message);
}
