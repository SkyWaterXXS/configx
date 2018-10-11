package com.github.skywaterxxs.configx.core.model;

import java.io.Serializable;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.MethodSpecial</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class MethodSpecial implements Serializable {
    private static final long serialVersionUID = 1L;

    static public final String left = "[";
    static public final String right = "]";
    static public final String equal = "=";
    static public final String split = "#";
    static public final String KEY_TIMEOUT = "clientTimeout";

    static public MethodSpecial parseMethodSpecial(String str) {
        int idx_split = str.indexOf(split); // "#"被认为是MethodSpecial配置的标志
        if (idx_split < 0) {
            return null;
        }

        int idx_equal = str.indexOf(equal);
        int idx_leftLeft = str.indexOf(left);
        int idx_leftRight = str.indexOf(right);
        int idx_rightLeft = str.indexOf(left, idx_equal);
        int idx_rightRight = str.indexOf(right, idx_equal);

        String methodName = str.substring(idx_leftLeft + 1, idx_leftRight);
        String key = str.substring(idx_rightLeft + 1, idx_split);
        String value = str.substring(idx_split + 1, idx_rightRight);

        MethodSpecial special = new MethodSpecial();
        special.setMethodName(methodName);
        if (KEY_TIMEOUT.equals(key)) {
            special.setClientTimeout(Long.parseLong(value));
        }
        return special;
    }

    private String methodName;

    private long clientTimeout = 3000;

    private int retries = 0;

    public long getClientTimeout() {
        return clientTimeout;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        if (retries > 0) {
            this.retries = retries;
        }
    }

    public void setClientTimeout(long clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(left).append(methodName).append(right);
        sb.append(equal).append(left);
        sb.append(KEY_TIMEOUT).append(split);
        sb.append(clientTimeout).append(right);
        return sb.toString();
    }
}