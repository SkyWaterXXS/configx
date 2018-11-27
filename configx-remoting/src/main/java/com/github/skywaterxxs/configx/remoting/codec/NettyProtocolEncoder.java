package com.github.skywaterxxs.configx.remoting.codec;

//import com.github.skywaterxxs.configx.remoting.BaseHeader;
import com.github.skywaterxxs.configx.remoting.ByteBufferWrapper;
import com.github.skywaterxxs.configx.remoting.Protocol;
import com.github.skywaterxxs.configx.remoting.ProtocolFactory;
import com.github.skywaterxxs.configx.remoting.protocol.StringMessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.codec.NettyProtocolEncoder</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class NettyProtocolEncoder extends MessageToByteEncoder<Object> {
    public NettyProtocolEncoder() {
        super(false);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
        NettyByteBufferWrapper byteBufferWrapper = new NettyByteBufferWrapper(out);
        this.encode(message,byteBufferWrapper);
    }


    private void encode(Object message,ByteBufferWrapper bytebufferWrapper) throws Exception {

        if (message instanceof String){
            Protocol protocol=new StringMessageProtocol();
            protocol.encode( message,bytebufferWrapper);

        }


    }
}
