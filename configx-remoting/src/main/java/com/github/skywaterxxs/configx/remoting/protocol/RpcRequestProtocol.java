package com.github.skywaterxxs.configx.remoting.protocol;

import com.github.skywaterxxs.configx.remoting.ByteBufferWrapper;
import com.github.skywaterxxs.configx.remoting.Protocol;
import com.github.skywaterxxs.configx.remoting.RemotingConstants;
import com.github.skywaterxxs.configx.remoting.RpcRequest;
import com.github.skywaterxxs.configx.remoting.util.ThreadLocalCache;


/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.protocol.RpcRequestProtocol</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/2 </p>
 * 请求：
 * 字节  描述
 * 1   标志HSF2协议
 * 2   版本
 * 3   请求
 * 4   序列化方式
 * 5～7 保留字节
 * 8～15    请求ID
 * 16～19   超时时间
 * 20～35   服务名，方法名，方法参数值的长度
 * 不等  服务名，方法名，方法参数值的值
 * +4  附加信息长度
 * 不等  附加信息值
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RpcRequestProtocol implements Protocol {

    public static final int REQUEST_HEADER_LEN = 1 * 6 + 6 * 4;

    /**
     * encode message to byte
     *
     * @param message           原始消息
     * @param byteBufferWrapper 编码后的字节
     * @throws Exception exception
     */
    @Override
    public void encode(Object message, ByteBufferWrapper byteBufferWrapper) throws Exception {

        if (!(message instanceof RpcRequest)) {
            return;
        }

        RpcRequest rpcRequest=(RpcRequest)message;
        
        int requestArgTypesLen = 0;
        int requestArgsLen = 0;
        String[] argTypes = rpcRequest.getArgTypes();
        byte[][] requestArgTypes = new byte[argTypes.length][];
        for (int i = 0; i < argTypes.length; i++) {
            requestArgTypes[i] = ThreadLocalCache.getBytes(argTypes[i]);
        }

        for (byte[] requestArgType : requestArgTypes) {
            requestArgTypesLen += requestArgType.length;
        }
        byte[][] requestObjects = rpcRequest.getRequestObjects();
        if (requestObjects != null) {
            for (byte[] requestArg : requestObjects) {
                requestArgsLen += requestArg.length;
            }
        }
        byte[] targetInstanceNameBytes = ThreadLocalCache.getBytes(rpcRequest.getTargetInstanceName());
        byte[] methodNameBytes = ThreadLocalCache.getBytes(rpcRequest.getMethodName());
        long id = rpcRequest.getRequestID();
        int timeout = rpcRequest.getTimeout();
        int requestArgTypesCount = requestArgTypes.length;
        int requestPropLength = rpcRequest.getRequestProps() == null ? 0 : rpcRequest.getRequestProps().length;
        int capacity = REQUEST_HEADER_LEN + requestArgTypesCount * 4 * 2 + 5
                + targetInstanceNameBytes.length + methodNameBytes.length + requestArgTypesLen + requestArgsLen
                + requestPropLength;
        byteBufferWrapper.ensureCapacity(capacity);
        byteBufferWrapper.writeByte(RemotingConstants.PROCOCOL_VERSION_HSF_REMOTING);
        byteBufferWrapper.writeByte(rpcRequest.getCodecType());
        byteBufferWrapper.writeBytes(rpcRequest.EXTENED_BYTES);
        byteBufferWrapper.writeLong(id);
        byteBufferWrapper.writeInt(timeout);
        byteBufferWrapper.writeInt(targetInstanceNameBytes.length);
        byteBufferWrapper.writeInt(methodNameBytes.length);
        byteBufferWrapper.writeInt(requestArgTypesCount);
        for (byte[] requestArgType : requestArgTypes) {
            byteBufferWrapper.writeInt(requestArgType.length);
        }
        if (requestObjects != null) {
            for (byte[] requestArg : requestObjects) {
                byteBufferWrapper.writeInt(requestArg.length);
            }
        }
        byteBufferWrapper.writeInt(requestPropLength);
        byteBufferWrapper.writeBytes(targetInstanceNameBytes);
        byteBufferWrapper.writeBytes(methodNameBytes);
        for (byte[] requestArgType : requestArgTypes) {
            byteBufferWrapper.writeBytes(requestArgType);
        }
        if (requestObjects != null) {
            for (byte[] requestArg : requestObjects) {
                byteBufferWrapper.writeBytes(requestArg);
            }
        }
        if (rpcRequest.getRequestProps() != null) {
            byteBufferWrapper.writeBytes(rpcRequest.getRequestProps());
        }
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
        if (byteBufferWrapper.readableBytes() < REQUEST_HEADER_LEN - 2) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }
        byte codecType = byteBufferWrapper.readByte();
        // ignore the extended three bytes
        byteBufferWrapper.setReaderIndex(byteBufferWrapper.readerIndex() + 3);
        long requestId = byteBufferWrapper.readLong();
        int timeout = byteBufferWrapper.readInt();
        int targetInstanceLen = byteBufferWrapper.readInt();
        int methodNameLen = byteBufferWrapper.readInt();
        int argsCount = byteBufferWrapper.readInt();
        int argInfosLen = argsCount * 4 * 2;
        int expectedLenInfoLen = argInfosLen + targetInstanceLen + methodNameLen + 4;
        int size = expectedLenInfoLen;
        if (byteBufferWrapper.readableBytes() < expectedLenInfoLen) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }
        int expectedLen = 0;
        int[] argsTypeLen = new int[argsCount];
        for (int i = 0; i < argsCount; i++) {
            argsTypeLen[i] = byteBufferWrapper.readInt();
            expectedLen += argsTypeLen[i];
        }
        int[] argsLen = new int[argsCount];
        for (int i = 0; i < argsCount; i++) {
            argsLen[i] = byteBufferWrapper.readInt();
            expectedLen += argsLen[i];
        }
        int requestPropLength = byteBufferWrapper.readInt();
        expectedLen += requestPropLength;
        byte[] targetInstanceByte = new byte[targetInstanceLen];
        byteBufferWrapper.readBytes(targetInstanceByte);
        byte[] methodNameByte = new byte[methodNameLen];
        byteBufferWrapper.readBytes(methodNameByte);

        size += expectedLen;
        if (byteBufferWrapper.readableBytes() < expectedLen) {
            byteBufferWrapper.setReaderIndex(originPosition);
            return null;
        }
        byte[][] argTypes = new byte[argsCount][];
        for (int i = 0; i < argsCount; i++) {
            byte[] argTypeByte = new byte[argsTypeLen[i]];
            byteBufferWrapper.readBytes(argTypeByte);
            argTypes[i] = argTypeByte;
        }
        byte[][] args = new byte[argsCount][];
        for (int i = 0; i < argsCount; i++) {
            byte[] argByte = new byte[argsLen[i]];
            byteBufferWrapper.readBytes(argByte);
            args[i] = argByte;
        }
        byte[] requestPropBytes = new byte[requestPropLength];
        byteBufferWrapper.readBytes(requestPropBytes);

        String[] argTypeStrings = new String[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            argTypeStrings[i] = ThreadLocalCache.getString(argTypes[i]);
        }

        return new RpcRequest(requestId, timeout, ThreadLocalCache.getString(targetInstanceByte),
                ThreadLocalCache.getString(methodNameByte), argTypeStrings, args, requestPropBytes, codecType, size);
    }
}
