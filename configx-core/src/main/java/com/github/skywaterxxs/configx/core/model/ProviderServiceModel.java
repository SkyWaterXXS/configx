package com.github.skywaterxxs.configx.core.model;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ProviderServiceModel</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProviderServiceModel {
    private final String serviceName;
    private final Object serviceInstance;
    private final ServiceMetadata metadata;
    private final Map<String, List<ProviderMethodModel>> methods = new HashMap<String, List<ProviderMethodModel>>();
    private final Method injectConsumerIpMethod;


    //can't retrive this value with ConfigurationService due to "maven cyclic reference"
    public static final String HSF_USE_POJO_METHODS = "hsf.usepojomethod";
    private boolean isUsePojoMethods = false;

    public ProviderServiceModel(String serviceName, ServiceMetadata metada, Object serviceInstance) {
        if (null == serviceInstance) {
            throw new IllegalArgumentException("服务[" + serviceName + "]的Target为NULL.");
        }

        this.serviceName = serviceName;
        this.metadata = metada;
        this.serviceInstance = serviceInstance;

        String prop = System.getProperty(HSF_USE_POJO_METHODS);
        if(prop!= null && prop.equals("true")){
            isUsePojoMethods = true;
        }

        initMethod();
        this.injectConsumerIpMethod = getMethodToInjectCosumerIP(metada, serviceName);
    }

    public Method getInjectConsumerIpMethod() {
        return injectConsumerIpMethod;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceMetadata getMetadata() {
        return metadata;
    }

    public Object getServiceInstance() {
        return serviceInstance;
    }

    public List<ProviderMethodModel> getAllMethods() {
        List<ProviderMethodModel> result = new ArrayList<ProviderMethodModel>();
        for (List<ProviderMethodModel> models : methods.values()) {
            result.addAll(models);
        }
        return result;
    }

    public ProviderMethodModel getMethodModel(String methodName, String[] argTypes) {
        List<ProviderMethodModel> methodModels = methods.get(methodName);
//        if(LoggerInit.LOGGER.isDebugEnabled() && (methodModels == null || methodModels.size() ==0 )){
//            LoggerInit.LOGGER.debug("服务端不提供[" + methodName +"]方法，请检查二方包版本是否需要升级.\n");
//        }
        if (methodModels != null) {
            for (ProviderMethodModel methodModel : methodModels) {
                if (Arrays.equals(argTypes, methodModel.getMethodArgTypes())) {
                    return methodModel;
                }
            }
//            if(LoggerInit.LOGGER.isDebugEnabled()){
//                LoggerInit.LOGGER.debug("服务端的参数类型与收到的请求中的参数类型不匹配，请检查参数类型是否正确.\n");
//            }
        }
        return null;
    }

    private Method getMethodToInjectCosumerIP(ServiceMetadata metadata, String serviceUniqueNamel) {
        final String methodName = metadata.getProperty(ServiceMetadata.METHOD_TO_INJECT_CONSUMERIP_PROP_KEY);
        if (methodName == null) {
            // 如果HSFSpringProviderBean中没有显式配置，则不去试图调用设置客户端IP的方法
            return null;
        }
        ProviderMethodModel pm = this.getMethodModel(methodName, new String[] { String.class.getName() });
        return pm == null ? null : pm.getMethod();
    }

    private int getServiceTimeoutValue(ServiceMetadata metadata, String methodName) {
        if (metadata.getMethodSpecial(methodName) != null) {
            return (int) metadata.getMethodSpecial(methodName).getClientTimeout();
        } else {
            return metadata.getTimeout();
        }
    }

    private void initMethod() {
        Method[] methodsToExport = null;
        if(isUsePojoMethods){
            methodsToExport = serviceInstance.getClass().getMethods();
        }else{
            methodsToExport = metadata.getIfClazz().getMethods();
        }

        for (Method method : methodsToExport) {
            method.setAccessible(true);

            List<ProviderMethodModel> methodModels = methods.get(method.getName());
            if (methodModels == null) {
                methodModels = new ArrayList<ProviderMethodModel>(1);
                methods.put(method.getName(), methodModels);
            }
            methodModels.add(new ProviderMethodModel(method, serviceName, this.getServiceTimeoutValue(this.metadata,
                    method.getName())));
        }
    }

}
