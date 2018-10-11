package com.github.skywaterxxs.configx.remoting.server;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.server.Server</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
 public interface Server {

    /**
     * start server at listenPort,requests will be handled in businessThreadPool
     */
     void start(int listenPort) throws Exception;

    /**
     * support http transport in another port
     *
     * @param listenPort
     * @throws Exception
     */
//     void startHttp(int listenPort) throws Exception;

    /**
     * stop server
     */
     void stop() throws Exception;

    void signalClosingServer();

    /**
     * support to be on cloud using LVS
     */
     void closeConnections();
}
