package com.github.skywaterxxs.configx.remoting;

//import com.github.skywaterxxs.configx.remoting.server.RpcServerHandler;
import com.github.skywaterxxs.configx.remoting.protocol.RpcRequestProtocol;
import com.github.skywaterxxs.configx.remoting.protocol.RpcResponseProtocol;
import com.github.skywaterxxs.configx.remoting.server.ServerHandler;

import static com.github.skywaterxxs.configx.remoting.RemotingConstants.PROTOCOL_RPC_REQUEST;
import static com.github.skywaterxxs.configx.remoting.RemotingConstants.PROTOCOL_RPC_RESPONSE;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.ProtocolFactory</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProtocolFactory {

    private final Protocol[] protocolHandlers = new Protocol[256];

    public static ProtocolFactory instance = new ProtocolFactory();

    private ProtocolFactory() {
        registerProtocol(PROTOCOL_RPC_REQUEST, new RpcRequestProtocol());
        registerProtocol(PROTOCOL_RPC_RESPONSE, new RpcResponseProtocol());

//        registerProtocol(RemotingConstants.PROCOCOL_VERSION_HEATBEAT, new HeartBeatProtocol());
//        registerProtocol(RemotingConstants.PROCOCOL_VERSION_TB_REMOTING, new TbRemotingProtocol());
//        registerProtocol(RemotingConstants.PROCOCOL_VERSION_DUBBO_REMOTING, new DubboProtocol());
    }


    private void registerProtocol(byte protocolType, Protocol customProtocol) {
        if (protocolHandlers[protocolType & 0x0FF] != null) {
            throw new RuntimeException("protocol header's sign is overlapped");
        }
        protocolHandlers[protocolType & 0x0FF] = customProtocol;
    }

    public Protocol getProtocol(byte protocolType) {
        return protocolHandlers[protocolType & 0x0FF];
    }


    public Protocol getProtocol(Object object) {

        Protocol protocol = null;

        if (object instanceof RpcRequest) {
            protocol = getProtocol(PROTOCOL_RPC_REQUEST);
        } else if (object instanceof RpcResponse) {
            protocol = getProtocol(PROTOCOL_RPC_RESPONSE);
        }


        return protocol;
    }
}
