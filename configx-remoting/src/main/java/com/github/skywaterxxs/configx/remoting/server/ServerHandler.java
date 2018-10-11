package com.github.skywaterxxs.configx.remoting.server;

import com.github.skywaterxxs.configx.remoting.Connection;

import java.util.concurrent.Executor;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.server.ServerHandler</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ServerHandler<T> {
     void handleRequest(final T request, final Connection connection, final long startTime);

     Executor getExecutor(final T request);

}
