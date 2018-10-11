package com.github.skywaterxxs.configx.core.model;

import com.github.skywaterxxs.configx.core.HSFConstants;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ConsumerMethodModel</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConsumerMethodModel {
    private static final String TIMEOUT_TYPE_KEY = "_TIMEOUT";
    private static final String SYNC_INVOKE = "SYNC";

    private final Method method;
    private final String enterMsg;
    private final ServiceMetadata metadata;
    private final boolean isCallBack;
    private final boolean isFuture;
    private final String[] parameterTypes;
    private final Class<?>[] parameterClasses;
    private final Class<?> returnClass;
    private final String methodNameToLog;
    private final String methodName;
    private final int executeTimes;
    private final boolean isGeneric;
    private final String invokeType;
    private final boolean isReliable;

    private final int timeout;

    public ConsumerMethodModel(Method method, ServiceMetadata metadata) {
        this.method = method;
        this.parameterClasses = method.getParameterTypes();
        this.returnClass = method.getReturnType();
        this.parameterTypes = this.createParamSignature(parameterClasses);
        this.methodName = method.getName();
        this.metadata = metadata;
        this.isGeneric = isGeneric();
        this.enterMsg = "invoke service:" + this.getUniqueName() + this.methodName
                + Arrays.toString(this.parameterTypes);
        this.isCallBack = metadata.isCallBackMethod(this.methodName);
        this.isFuture = metadata.isFutureMethod(this.methodName);
        if (this.isGeneric) {
            this.methodNameToLog = ServiceIndexUtil.getMethodNameToLog(parameterTypes[0],
                    Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length));
        } else {
            this.methodNameToLog = ServiceIndexUtil.getMethodNameToLog(methodName, parameterTypes);
        }
        this.executeTimes = metadata.getRetries(methodName) + 1;
        this.isReliable = metadata.isReliableMethod(this.methodName);
        this.timeout = createTimeout();
        this.invokeType = judgeInvokeType(metadata);
    }

    private String judgeInvokeType(ServiceMetadata metadata) {
        if (isCallBack) {
            return HSFConstants.INVOKE_TYPE_CALL_BACK;
        } else if (isFuture) {
            return HSFConstants.INVOKE_TYPE_FUTURE;
        } else if (isReliable) {
            return HSFConstants.INVOKE_TYPE_RELIABLE;
        } else {
            return SYNC_INVOKE;
        }
    }

    /**
     * Top方式
     *
     * @param methodName
     * @param parameterTypes
     * @param metadata
     */
    public ConsumerMethodModel(String methodName, String[] parameterTypes, ServiceMetadata metadata) {
        this.method = null;
        this.parameterClasses = null;
        this.returnClass = Map.class;
        this.metadata = metadata;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.enterMsg = "invoke service:" + this.getUniqueName() + this.methodName
                + Arrays.toString(this.parameterTypes);
        this.isCallBack = metadata.isCallBackMethod(this.methodName);
        this.isFuture = metadata.isFutureMethod(this.methodName);
        this.isGeneric = false;
        this.methodNameToLog = ServiceIndexUtil.getMethodNameToLog(methodName, parameterTypes);
        this.executeTimes = metadata.getRetries(methodName) + 1;
        this.isReliable = metadata.isReliableMethod(this.methodName);
        this.timeout = createTimeout();
        this.invokeType = judgeInvokeType(metadata);
    }

    public int getTimeout() {
        return timeout;
    }

    public String getMethodNameToLog() {
        return methodNameToLog;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public String getInvokeType() {
        return invokeType;
    }

    public boolean isReliable() {
        return isReliable;
    }

    public String getUniqueName() {
        return metadata.getUniqueName();
    }

    public String getEnterMsg() {
        return enterMsg;
    }

    public boolean isCallBackMethod() {
        return isCallBack;
    }

    public boolean isFutureMethod() {
        return isFuture;
    }

    public boolean isAsyncMethod() {
        return isCallBack || isFuture;
    }

    /**
     * return True methodName
     *
     * @param request
     * @return
     */
    public String getMethodName(HSFRequest request) {
        if (isGeneric) {
            return request.getMethodArgs()[0].toString();
        }
        return methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public ServiceMetadata getMetadata() {
        return metadata;
    }

    /**
     * 获取参数类型
     */
    private String[] createParamSignature(Class<?>[] args) {
        if (args == null || args.length == 0) {
            return new String[] {};
        }
        String[] paramSig = new String[args.length];
        for (int x = 0; x < args.length; x++) {
            paramSig[x] = args[x].getName();
        }
        return paramSig;
    }

    private int createTimeout() {
        if (isReliable) {
            return 1000;
        }

        MethodSpecial special = metadata.getMethodSpecial(methodName);
        if (special == null && isGeneric) {
            // HSF xml方式配置的超时时间，key是methodName，Dubbo Reference方式配置的，key是$invoke
            special = metadata.getMethodSpecial("$invoke");
        }
        if (null != special) {
            return (int) special.getClientTimeout();
        }
        special = metadata.getMethodSpecial("*");
        if (null != special) {
            return (int) special.getClientTimeout();
        } else if (metadata.getProperty(TIMEOUT_TYPE_KEY) != null) {
            // 这里是够会覆盖RemotingRPCProtocolComponent.getReadTimeout()中的return targetURL.getTimeout()?
            return Integer.parseInt(metadata.getProperty(TIMEOUT_TYPE_KEY));
        }
        return 0;
    }

    private boolean isGeneric() {
        return methodName.equals(HSFConstants.$INVOKE) && parameterTypes != null && parameterTypes.length == 3;
    }

    public String getSecureKey() {
        return metadata.getSecureKey();
    }

    public int getExecuteTimes() {
        return executeTimes;
    }

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }
}

