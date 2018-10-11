package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.BaseResponse</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseResponse extends BaseHeader{
    private ResponseStatus status = ResponseStatus.OK;

    public BaseResponse(int protocolType, long requestId) {
        super(protocolType, requestId);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public void setStatus(byte status) {
        this.status = ResponseStatus.fromCode(status);
    }

    public String toString() {
        return this.getProtocolType() + ":" + status;
    }

//    public abstract HSFResponse getResponseObject(BaseRequest request);
}
