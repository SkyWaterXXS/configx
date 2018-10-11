package com.github.skywaterxxs.configx.remoting.protocol;

import com.github.skywaterxxs.configx.remoting.ByteBufferWrapper;
import com.github.skywaterxxs.configx.remoting.Protocol;
import com.github.skywaterxxs.configx.remoting.RemotingConstants;
import com.github.skywaterxxs.configx.remoting.RpcResponse;

import static com.github.skywaterxxs.configx.remoting.RpcResponse.EXTENED_BYTES;


/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.protocol.RpcResponseProtocol</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/2 </p>
 * 响应：
 * 字节  描述
 * 1   标志HSF2协议
 * 2   版本
 * 3   响应
 * 4   状态code
 * 5   序列化方式
 * 6～8 保留字节
 * 9～16    对应的请求ID
 * 17～20   返回值的长度大小
 * 不等  返回值的值
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RpcResponseProtocol implements Protocol {

    public static final int RESPONSE_HEADER_LEN = 1 * 8 + 4 * 3;

    /**
     * encode message to byte
     *
     * @param message           原始消息
     * @param byteBufferWrapper 编码后的字节
     * @throws Exception exception
     */
    @Override
    public void encode(Object message, ByteBufferWrapper byteBufferWrapper) throws Exception {

        if (!(message instanceof RpcResponse)) {
            return;
        }
        RpcResponse rpcResponse=(RpcResponse)message;

        byte[] body = rpcResponse.getResponse();
        long id = rpcResponse.getRequestID();
        int capacity = RESPONSE_HEADER_LEN + body.length;
        byteBufferWrapper.ensureCapacity(capacity);
        byteBufferWrapper.writeByte(RemotingConstants.PROCOCOL_VERSION_HSF_REMOTING);
        byteBufferWrapper.writeByte(rpcResponse.getStatus().getCode());
        byteBufferWrapper.writeByte(rpcResponse.getCodecType());
        byteBufferWrapper.writeBytes(EXTENED_BYTES);
        byteBufferWrapper.writeLong(id);
        byteBufferWrapper.writeInt(body.length);
        byteBufferWrapper.writeBytes(body);
    }

    /**
     * decode byte to message
     *
     * @param byteBufferWrapper 原始字节
     * @param originPosition    原始位置
     * @return 解析后的Object
     * @throws Exception exception
     */
    @Override
    public Object decode(ByteBufferWrapper byteBufferWrapper, int originPosition) throws Exception {
        if (byteBufferWrapper.readableBytes() < RESPONSE_HEADER_LEN - 2) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }
        byte status = byteBufferWrapper.readByte();
        byte codecType = byteBufferWrapper.readByte();
        // ignore the extended three bytes
        byteBufferWrapper.setReaderIndex(byteBufferWrapper.readerIndex() + 3);
        long requestId = byteBufferWrapper.readLong();
        int bodyLen = byteBufferWrapper.readInt();
        if (byteBufferWrapper.readableBytes() < bodyLen) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }

        byte[] bodyBytes = new byte[bodyLen];
        byteBufferWrapper.readBytes(bodyBytes);
        return new RpcResponse(requestId, codecType, status, bodyBytes);
    }
}
