package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.ResponseStatus</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ResponseStatus {

    OK(20, "OK"), // 正常成功
    CLIENT_TIMEOUT(30, "client timeout"), //
    SERVER_TIMEOUT(31, "server timeout"), //
    BAD_REQUEST(40, "bad request"), //
    BAD_RESPONSE(50, "bad response"), //
    SERVICE_NOT_FOUND(60, "service not found"), //
    SERVICE_ERROR(70, "service error"), //
    SERVER_ERROR(80, "server error"), //
    CLIENT_ERROR(90, "client error"), //
    UNKNOWN_ERROR(91, "Unknow error"), // 没有注册Listener，包括CheckMessageListener和MessageListener
    THREAD_POOL_BUSY(81, "Thread pool is busy"), // 响应段线程繁忙
    COMM_ERROR(82, "Communication error"), //
    SERVER_CLOSING(88, "server will close soon"), //
    SERVER_GET_CODER(10, "server send coders"), //
    UNKNOWN_CODE(83, "Unkown code"); // 通讯错误，如编码错误

    private final String message;
    private final byte code;

    private ResponseStatus(final int code, final String message) {
        this.code = (byte) code;
        this.message = message;
    }

    public static ResponseStatus fromCode(byte code) {
        ResponseStatus[] values = ResponseStatus.values();
        for (ResponseStatus value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN_CODE;
    }

    public String getMessage() {
        return this.message;
    }

    public byte getCode() {
        return code;
    }
}
