package com.github.skywaterxxs.configx.remoting.codec;

import com.github.skywaterxxs.configx.remoting.ByteBufferWrapper;
import com.github.skywaterxxs.configx.remoting.Protocol;
import com.github.skywaterxxs.configx.remoting.ProtocolFactory;
import com.github.skywaterxxs.configx.remoting.protocol.StringMessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.codec.NettyProtocolDecoder</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyProtocolDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        NettyByteBufferWrapper wrapper = new NettyByteBufferWrapper(in, ctx.channel());
        Object msg = this.decode(wrapper);
        if (msg != null) {
            out.add(msg);
        }
    }

    private Object decode(ByteBufferWrapper wrapper) throws Exception {
        final int originPos = wrapper.readerIndex();
        if (wrapper.readableBytes() < 1) {
            wrapper.setReaderIndex(originPos);
            return null;
        }
        byte protocolType = wrapper.readByte();
        System.out.println("protocolType="+protocolType);
        Protocol protocol = new StringMessageProtocol();


        return protocol.decode(wrapper, originPos);
    }

}
