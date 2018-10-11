package com.github.skywaterxxs.configx.remoting;

import com.github.skywaterxxs.configx.remoting.server.ServerHandler;
import com.github.skywaterxxs.configx.remoting.util.UUIDGenerator;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.BaseRequest</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseRequest extends BaseHeader{
    private final int timeout;

    public BaseRequest(int protocolType, long id, int timeout) {
        super(protocolType, id);
        this.timeout = timeout;
    }

    public BaseRequest(int protocolType, int timeout) {
        this(protocolType, UUIDGenerator.getNextOpaque(), timeout);
    }

    public BaseRequest(int protocolType) {
        this(protocolType, RemotingConstants.DEFAULT_TIMEOUT);
    }

    public abstract BaseResponse createErrorResponse(final String errorInfo);
    public abstract ServerHandler<? extends BaseRequest> getServerHandler();

    public int getTimeout() {
        return timeout;
    }

}
