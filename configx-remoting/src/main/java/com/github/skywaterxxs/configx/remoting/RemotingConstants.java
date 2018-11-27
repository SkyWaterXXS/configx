package com.github.skywaterxxs.configx.remoting;

import java.nio.charset.Charset;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.netting.RemotingConstants</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RemotingConstants {

    byte PROTOCOL_STRING = (byte) 1;

//    byte PROTOCOL_RPC_REQUEST = (byte) 1;
//    byte PROTOCOL_RPC_RESPONSE = (byte) 2;


    /** TB_REMOTING新版报文的第一个byte的内容，用来标识新版报文 */
     byte PROCOCOL_VERSION_TB_REMOTING = (byte) 1;

    /** HSF REMOTING新版报文的第一个byte的内容，用来标识新版报文 */
     byte PROCOCOL_VERSION_HSF_REMOTING = (byte) 0x0E;

    /** DUBBO新版报文的第一个byte的内容，用来标识新版报文 */

    /** Heartbeat新版报文的第一个byte的内容，用来标识新版报文 */
     byte PROCOCOL_VERSION_HEATBEAT = (byte) 0x0C;

    /** Handshake新版报文的第一个byte的内容，用来标识新版报文 */
     byte PROCOCOL_VERSION_HANDSHAKE = (byte) 0x0F;

     String CONNECTION_NUM_PER_URL = "connection_num";

    // heartbeat cycle is 27 seconds
     int DEFAULT_HEARTBEAT_PERIAD = 27;

    // 默认的超时时间， 1秒
    // change default value from 1s to 3s since 2.1.0.0
     int DEFAULT_TIMEOUT = 3000;

     Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

     String URL_PREFIX_DUBBO = "dubbo";
     String URL_PREFIX_HSF2 = "hsf";
     String URL_PREFIX_HSF1 = "";

     String CLIENT_SEND_LIMIT = "sendLimit";

     String DUBBO_VERSION = "2.5.4";

     String INVALID_CAUSE_CLOSE = "invalid call is removed because of connection closed";
     String USELESS_OF_TIMEOUT = "invalid call because of timeout:";

    /**
     * heartbeat 使用的hessian2的序列化NULL
     */
     byte[] DUBBO2_HEARTBEAT_BODY = { 78 };
    
}
