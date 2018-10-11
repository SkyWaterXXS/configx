package com.github.skywaterxxs.configx.core.model;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ProviderMethodModel</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProviderMethodModel {

    private final static AtomicInteger INDEX_GENERATOR = new AtomicInteger(1);
//    private final static JaketTypeBuilder builder = new JaketTypeBuilder();

    private transient final Method method;
    private final String methodName;
    private final String[] methodArgTypes;
    private transient final String methodKey;
    private transient final String methodLogname;
    private final String serviceName;
    private transient final int index = INDEX_GENERATOR.getAndIncrement();
    private transient final int timeout;
    private volatile int responseTime;
    private final AtomicInteger elapseTime = new AtomicInteger();
    private final AtomicInteger invokeCount = new AtomicInteger();
    private final AtomicInteger threadCount = new AtomicInteger(0);
    private final String spasMethodName;

    public ProviderMethodModel(Method method, String serviceName, int timeout) {
        this.method = method;
        this.serviceName = serviceName;
        this.methodName = method.getName();

        StringBuilder methodkeyBuilder = new StringBuilder(method.getName());
        StringBuilder logMethodBuilder = new StringBuilder(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            this.methodArgTypes = new String[parameterTypes.length];
            logMethodBuilder.append("~");
            int index = 0;
            for (Class<?> paramType : parameterTypes) {
                methodArgTypes[index++] = paramType.getName();
                methodkeyBuilder.append(paramType.getName());
                int charIndex = paramType.getName().lastIndexOf('.') + 1;
                logMethodBuilder.append(paramType.getName().charAt(charIndex));
            }
        } else {
            this.methodArgTypes = new String[0];
        }

        this.methodLogname = logMethodBuilder.toString();
        this.methodKey = methodkeyBuilder.toString();
        this.timeout = timeout;
        Class<?>[] paramTypes = method.getParameterTypes();
        Type[] genericParamTypes = method.getGenericParameterTypes();

//        String[] pms = new String[paramTypes.length];
//        for (int i = 0; i < paramTypes.length; i++) {
//            try {
//                TypeDefinition td = builder.build(genericParamTypes[i], paramTypes[i]);
//                pms[i] = td.getType();
//            } catch (Throwable e) {
//                pms[i] = paramTypes[i].getName();
//            }
//        }
//        spasMethodName = (methodName + "#" + StringUtils.join(pms, '#')).replace(',', '#');

        spasMethodName="";
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodLogname() {
        return methodLogname;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodKeyWithServiceName() {
        return serviceName + "#" + methodKey;
    }

    public String[] getMethodArgTypes() {
        return methodArgTypes;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getIndex() {
        return index;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public AtomicInteger getThreadCount() {
        return threadCount;
    }

    public String getSpasMethodName() {
        return spasMethodName;
    }

    public AtomicInteger getElapseTime() {
        return elapseTime;
    }

    public AtomicInteger getInvokeCount() {
        return invokeCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(methodArgTypes);
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProviderMethodModel other = (ProviderMethodModel) obj;
        if (methodName == null) {
            if (other.methodName != null)
                return false;
        } else if (!methodName.equals(other.methodName))
            return false;
        if (!Arrays.equals(methodArgTypes, other.methodArgTypes))
            return false;
        return true;
    }

}
