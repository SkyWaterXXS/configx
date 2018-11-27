package com.github.skywaterxxs.configx.remoting.protocol;

import com.github.skywaterxxs.configx.remoting.ByteBufferWrapper;
import com.github.skywaterxxs.configx.remoting.Protocol;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.protocol.StringMessageProtocol</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringMessageProtocol implements Protocol {
    /**
     * encode message to byte
     * <p>
     * 0 协议类型
     * 1～4 消息长度
     * 5+ 消息内容
     *
     * @param message           原始消息
     * @param byteBufferWrapper 编码后的字节
     */
    @Override
    public void encode(Object message, ByteBufferWrapper byteBufferWrapper) {

        if (!(message instanceof String)) {
            return;
        }
        String string = (String) message;
        int capacity = 1 + 4 + string.getBytes().length;
        byteBufferWrapper.ensureCapacity(capacity);

        byteBufferWrapper.writeByte((byte) 7);
        byteBufferWrapper.writeInt(string.getBytes().length);
        byteBufferWrapper.writeBytes(string.getBytes());


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
    public String decode(ByteBufferWrapper byteBufferWrapper, int originPosition) {


        if (byteBufferWrapper.readableBytes() < 4) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }

        int stringLength = byteBufferWrapper.readInt();

        if (byteBufferWrapper.readableBytes() < stringLength) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }

        byte[] stingBytes = new byte[stringLength];

        byteBufferWrapper.readBytes(stingBytes);

        return new String(stingBytes);
    }
}
