package com.github.skywaterxxs.configx.remoting.exception;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.exception.ErrorCode</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ErrorCode implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SEVERITY_UNDEFINED = -1;
    public static final int SEVERITY_INFO = 0;
    public static final int SEVERITY_WARN = 1;
    public static final int SEVERITY_ERROR = 2;
    public static final int SEVERITY_FATAL = 3;

    private int code;
    private int severity;
    private String msg;

    /**
     * @return Returns the code.
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return Returns the severity.
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * @param severity
     *            The severity to set.
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /**
     * @return Returns the shortMessage.
     */
    public String getMessage() {
        return msg;
    }


    public void setMessage(String message) {
        this.msg = message;
    }

    /**
     * Returns the localized message; assumes message is a key
     *
     * @return
     */
    public String getLocalMessage() {
        return getMessage();
    }

    /**
     * Returns parameterized string
     *
     * @return
     */
    public String getFormattedMessage(Object[] args) {
        String localMsg = getLocalMessage();
        return MessageFormat.format(localMsg, args);
    }

}
