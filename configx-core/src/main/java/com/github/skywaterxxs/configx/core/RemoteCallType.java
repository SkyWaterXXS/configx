package com.github.skywaterxxs.configx.core;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.RemoteCallType</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public enum RemoteCallType {

    FUTURE("future"), CALLBACK("callback");

    private String type;

    private RemoteCallType(String type) {
        this.type = type;
    }

    public String getCallType() {
        return type;
    }
}