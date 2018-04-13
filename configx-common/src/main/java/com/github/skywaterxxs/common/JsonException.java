package com.github.skywaterxxs.common;

/**
 * @author xuxiaoshuo 2018/4/11
 */
public class JsonException extends RuntimeException {

    private static final long serialVersionUID = -9198606590046525595L;

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }
}