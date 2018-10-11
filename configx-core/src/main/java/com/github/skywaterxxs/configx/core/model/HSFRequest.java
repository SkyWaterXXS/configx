package com.github.skywaterxxs.configx.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.HSFRequest</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class HSFRequest implements Serializable {
    private static final long serialVersionUID = -7323141575870688636L;

    private String targetServiceUniqueName;
    private String methodName;
    private String[] methodArgSigs;
    private transient Object[] methodArgs; // 单独做序列化

    // 服务消费者IP
    private String localAddr;
    // 存放调用上下文序列化之后的结果，直接存放bytes，服务端就不需要因为反序列化而包含客户端的Context类了
    private byte[] invokeContext;
    // 是否需要可靠回调
    private boolean isNeedReliableCallback;
    // 请求中的其他配置属性
    private Map<String, Object> requestProps;

    private transient byte serializeType;
    private transient Class<?>[] parameterClasses;
    private transient Class<?> returnClass;

    public byte getSerializeType() {
        return serializeType;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    public void setSerializeType(byte serializeType) {
        this.serializeType = serializeType;
    }

    public byte[] getInvokeContext() {
        return invokeContext;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public Object[] getMethodArgs() {
        return methodArgs;
    }

    public String[] getMethodArgSigs() {
        return methodArgSigs;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getRequestProp(String key) {
        if (requestProps != null) { // 防止反序列化时候，没有初始化
            return requestProps.get(key);
        }
        return null;
    }

    public void refreshRequestProp(Map<String, Object> properties) {
        this.requestProps = properties;
    }

    public String getTargetServiceUniqueName() {
        return targetServiceUniqueName;
    }

    public boolean isNeedReliableCallback() {
        return isNeedReliableCallback;
    }

    public void setInvokeContext(byte[] invokeContext) {
        this.invokeContext = invokeContext;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public void setMethodArgs(Object[] methodArgs) {
        this.methodArgs = methodArgs;
    }

    public void setMethodArgSigs(String[] methodArgSigs) {
        this.methodArgSigs = methodArgSigs;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setNeedReliableCallback(boolean isNeedReliableCallback) {
        this.isNeedReliableCallback = isNeedReliableCallback;
    }

    public void setRequestProps(String key, Object value) {
        if (requestProps == null) { // 防止反序列化时候，没有初始化
            requestProps = new HashMap<String, Object>(3);
        }
        requestProps.put(key, value);
    }

    public Map<String, Object> getRequestProps() {
        return requestProps;
    }

    public void setTargetServiceUniqueName(String targetServiceUniqueName) {
        this.targetServiceUniqueName = targetServiceUniqueName;
    }

    public String getMethodKey() {
        StringBuilder methodKeyBuilder = new StringBuilder(targetServiceUniqueName);
        methodKeyBuilder.append(methodName);
        for (int i = 0; i < methodArgSigs.length; i++) {
            methodKeyBuilder.append(methodArgSigs[i]);
        }
        return methodKeyBuilder.toString();
    }

    public String getMethodSignature() {
        StringBuilder methodSignatureBuilder = new StringBuilder(methodName);
        for (int i = 0; i < methodArgSigs.length; i++) {
            methodSignatureBuilder.append("_");
            methodSignatureBuilder.append(methodArgSigs[i]);
        }
        return methodSignatureBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HSFRequest[");
        sb.append("服务名=").append(targetServiceUniqueName).append(", ");
        sb.append("方法名=").append(methodName).append(", ");
        sb.append("方法参数=[");
        if (null != methodArgs) {
            for (Object arg : methodArgs) {
                sb.append(arg).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]]");
        return sb.toString();
    }

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }

    public void setParameterClasses(Class<?>[] parameterClasses) {
        this.parameterClasses = parameterClasses;
    }

}