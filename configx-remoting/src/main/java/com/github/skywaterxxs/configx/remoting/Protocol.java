package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.netting.Protocol</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Protocol {


    /**
     * encode message to byte
     *
     * @param message           原始消息
     * @param byteBufferWrapper 编码后的字节
     * @throws Exception exception
     */
    void encode(Object message, ByteBufferWrapper byteBufferWrapper) throws Exception;

    /**
     * decode byte to message
     *
     * @param byteBufferWrapper 原始字节
     * @param originPosition    原始位置
     * @return 解析后的Object
     * @throws Exception exception
     */
    Object decode(ByteBufferWrapper byteBufferWrapper, int originPosition) throws Exception;

}
