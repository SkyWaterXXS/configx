package com.github.skywaterxxs.configx.remoting;

import java.net.SocketAddress;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.Connection</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Connection {

     SocketAddress getLocalAddress();

     SocketAddress getRemoteAddress();

     String getPeerIP();

     void refreshLastReadTime(long lastReadTime);

     long getLastReadTime();

//     void writeToChannel(BaseResponse responseWrapper);

     void close();
}
