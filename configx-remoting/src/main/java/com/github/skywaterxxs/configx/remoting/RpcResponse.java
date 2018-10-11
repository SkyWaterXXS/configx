package com.github.skywaterxxs.configx.remoting;


/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.RpcResponse</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RpcResponse extends BaseResponse{
    public static final byte[] EXTENED_BYTES = new byte[3];

    private final byte codecType;
    private final byte[] response;

    public RpcResponse(long requestId, byte codecType, ResponseStatus status, byte[] response) {
        super(RemotingConstants.PROCOCOL_VERSION_HSF_REMOTING, requestId);
        this.codecType = codecType;
        this.response = response;
        this.setStatus(status);
    }

    public RpcResponse(long requestId, byte codecType, byte status, byte[] response) {
        super(RemotingConstants.PROCOCOL_VERSION_HSF_REMOTING, requestId);
        this.codecType = codecType;
        this.response = response;
        this.setStatus(status);
    }

    public RpcResponse(long requestId, byte codecType, byte[] response) {
        super(RemotingConstants.PROCOCOL_VERSION_HSF_REMOTING, requestId);
        this.codecType = codecType;
        this.response = response;
    }

    public byte getCodecType() {
        return codecType;
    }

    public byte[] getResponse() {
        return response;
    }

//    public HSFResponse getResponseObject(BaseRequest request) {
//        HSFResponse response = new HSFResponse();
//        switch (this.getStatus()) {
//            case OK:
//                try {
//                    byte codecType = this.getCodecType();
//                    Object responseObject = null;
//                    if (codecType != Codecs.CUSTOMIZED_CODEC) {
//                        responseObject = Codecs.getDecoder(codecType).decode(this.getResponse());
//                    } else {
//                        responseObject = CustomizedSerializerHelper.getTransformer(((RpcRequest) request).getMethodKey())
//                                .decodeResult(this.getResponse());
//                    }
//                    //LoggerInit.LOGGER.debug(responseObject.toString());
//                    response.setAppResponse(responseObject);
//                } catch (Throwable e) {
//                    LoggerInit.LOGGER.error("","Deserialize error on client side",new RuntimeException(LoggerHelper.getErrorCodeStr("HSF","HSF-0037","业务问题","Deserialize error on client side"),e));
//                    response.setClientErrorMsg("[Remoting] Decode error on client side:" + e.getMessage());
//                    response.setAppResponse(e);
//                }
//                break;
//            case SERVICE_ERROR:
//            case SERVER_ERROR:
//            default:
//                response.setClientErrorMsg(new String(this.getResponse(), RemotingConstants.DEFAULT_CHARSET));
//                break;
//        }
//        return response;
//    }


    @Override
    public int size() {
        return response == null ? 0 : response.length;
    }

}
