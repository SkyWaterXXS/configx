package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.BaseHeader</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseHeader {
    private final int protocolType;
    // make sure it compatible to tbremoting
    private final long requestID;

    public BaseHeader(int protocolType, long requestID) {
        this.protocolType = protocolType;
        this.requestID = requestID;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public long getRequestID() {
        return requestID;
    }

    public abstract int size();
}
