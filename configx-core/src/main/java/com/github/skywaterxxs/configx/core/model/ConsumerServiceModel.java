package com.github.skywaterxxs.configx.core.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ConsumerServiceModel</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsumerServiceModel {
    private final ServiceMetadata metadata;
    private final Object proxyObject;

    private final Map<Method, ConsumerMethodModel> methodModels = new IdentityHashMap<Method, ConsumerMethodModel>();


    public ConsumerServiceModel(ServiceMetadata metadata, Object proxyObject, boolean isJava) {
        this.metadata = metadata;
        this.proxyObject = proxyObject;

        if (proxyObject != null) {
            Class<?> proxyClass = proxyObject.getClass();
            try {
                if (isJava) {
                    for (Field field : proxyClass.getDeclaredFields()) {
                        field.setAccessible(true);
                        Method method = (Method) field.get(this.proxyObject);
                        methodModels.put(method, new ConsumerMethodModel(method, metadata));
                    }
                } else {
                    Field field = proxyClass.getDeclaredField("methods");
                    field.setAccessible(true);
                    for (Method method : (Method[]) field.get(this.proxyObject)) {
                        methodModels.put(method, new ConsumerMethodModel(method, metadata));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ServiceMetadata getMetadata() {
        return metadata;
    }

    public Object getProxyObject() {
        return proxyObject;
    }

    public ConsumerMethodModel getMethodModel(Method method) {
        return methodModels.get(method);
    }
}