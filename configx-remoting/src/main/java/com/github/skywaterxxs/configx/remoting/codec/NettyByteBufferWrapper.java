package com.github.skywaterxxs.configx.remoting.codec;

import com.github.skywaterxxs.configx.remoting.BaseResponse;
import com.github.skywaterxxs.configx.remoting.ByteBufferWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.codec.NettyByteBufferWrapper</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyByteBufferWrapper implements ByteBufferWrapper {


    private static final String WRITE_RESPONSE_ERROR = "server write response error,request id is: ";
    private final ByteBuf buffer;
    private final Channel channel;

    public NettyByteBufferWrapper(ByteBuf buffer) {
        this(buffer, null);
    }

    public NettyByteBufferWrapper(ByteBuf buffer, Channel channel) {
        this.channel = channel;
        this.buffer = buffer;
    }

    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public void readBytes(byte[] dst) {
        buffer.readBytes(dst);
    }

    @Override
    public int readInt() {
        return buffer.readInt();
    }

    @Override
    public int readableBytes() {
        return buffer.readableBytes();
    }

    @Override
    public int readerIndex() {
        return buffer.readerIndex();
    }

    @Override
    public void setReaderIndex(int index) {
        buffer.setIndex(index, buffer.writerIndex());
    }

    @Override
    public void writeByte(byte data) {
        buffer.writeByte(data);
    }

    @Override
    public void writeBytes(byte[] data) {
        buffer.writeBytes(data);
    }

    @Override
    public void writeInt(int data) {
        buffer.writeInt(data);
    }

    @Override
    public void writeByte(int index, byte data) {
        buffer.writeByte(data);
    }

    @Override
    public void writeLong(long value) {
        buffer.writeLong(value);
    }

    @Override
    public long readLong() {
        return buffer.readLong();
    }

//    @Override
//    public void send(final BaseResponse object) {
//        ChannelFuture wf = channel.writeAndFlush(object);
//        wf.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (!future.isSuccess()) {
//                    LoggerInit.LOGGER_REMOTING.error("",
//                            LogConstants.PREFIX_IMPORTANT + WRITE_RESPONSE_ERROR + object.getRequestID());
//                }
//            }
//        });
//    }

    @Override
    public void ensureCapacity(int capacity) {
        buffer.capacity(capacity);
    }

}
