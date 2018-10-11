package com.github.skywaterxxs.configx.remoting;

import com.github.skywaterxxs.configx.remoting.server.ServerHandler;
import com.github.skywaterxxs.configx.remoting.util.UUIDGenerator;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.RpcRequest</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RpcRequest extends BaseRequest{

    public static final byte[] EXTENED_BYTES = new byte[3];

    private final String targetInstanceName;
    private final String methodName;
    private final String[] argTypes;
    private final byte[][] requestObjects;
    private final byte[] requestProps;
    private final byte codecType;
    private final int size;

    public RpcRequest(long id, int timeout, String targetInstanceName, String methodName, String[] argTypes,
                      byte[][] requestObjects, byte[] requestProps, byte codecType, int size) {
        super(RemotingConstants.PROCOCOL_VERSION_HSF_REMOTING, id, timeout);
        this.targetInstanceName = targetInstanceName;
        this.methodName = methodName;
        this.argTypes = argTypes;
        this.requestObjects = requestObjects;
        this.codecType = codecType;
        this.requestProps = requestProps;
        this.size = size;
    }

    public RpcRequest(int timeout, String targetInstanceName, String methodName, String[] argTypes,
                      byte[][] requestObjects, byte[] requestProps, byte codecType, int size) {
        this(UUIDGenerator.getNextOpaque(), timeout, targetInstanceName, methodName, argTypes, requestObjects,
                requestProps, codecType, size);
    }

    public byte[] getRequestProps() {
        return requestProps;
    }

    public String getTargetInstanceName() {
        return targetInstanceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getArgTypes() {
        return argTypes;
    }

    public byte[][] getRequestObjects() {
        return requestObjects;
    }

    public byte getCodecType() {
        return codecType;
    }

    public String getMethodKey() {
        return null;
//        return CustomizedSerializerHelper.catMethodName(targetInstanceName, methodName, argTypes);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public BaseResponse createErrorResponse(String errorInfo) {
        return null;
    }

    @Override
    public ServerHandler<? extends BaseRequest> getServerHandler() {
        return null;
    }
}
