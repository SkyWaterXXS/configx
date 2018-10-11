package com.github.skywaterxxs.configx.core.model;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.HSFExtensionLoader</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class HSFExtensionLoader {

    /**
     * 共享的容器，它不隶属与任何一个应用
     */
    private static final AppExtensionLoader SHARED_CONTAINER = new AppExtensionLoader();

    private static final ConcurrentHashMap<Object, ApplicationModel> INSTANCE_APP_MAP = new ConcurrentHashMap<Object, ApplicationModel>();

    /**
     * 创建一个AppServiceContainer
     *
     * @param applicationModel
     * @return
     */
    public static final AppExtensionLoader createAppExtensionLoader(ApplicationModel applicationModel) {
        return new AppExtensionLoader(applicationModel, SHARED_CONTAINER);
    }

    /**
     * <pre>
     * 根据一个接口类型，获取容器中的一个服务实例

     * 如果容器中有个实例，那么会选择返回一个
     * </pre>
     *
     * @param classType
     *            接口类型
     * @return 返回接口对应的扩展实例
     */
    public static <T> T getInstance(Class<T> classType) {
        ApplicationModel applicationModel = ApplicationModelFactory.getCurrentApplication();
        T instance = applicationModel.getAppExtensionLoader().getInstance(classType);
        if (instance != null) {
            recordOwner(classType, instance, applicationModel);
        }
        return instance;

    }

    /**
     * <pre>
     * 根据一个接口类型，获取容器中的一个服务实例

     * 如果容器中有个实例，那么会选择返回一个
     * </pre>
     *
     * @param classType
     *            接口类型
     * @return 返回接口对应的扩展实例
     */
    public static <T> T getInstance(Class<T> classType,ApplicationModel applicationModel) {
        T instance = applicationModel.getAppExtensionLoader().getInstance(classType);
        if (instance != null) {
            recordOwner(classType, instance, applicationModel);
        }
        return instance;

    }


    /**
     * <pre>
     * 根据接口类型，返回所有的扩展服务实例
     * </pre>
     *
     * @param classType
     *            接口类型
     * @return
     */
    @Deprecated
    public static <T> List<T> getInstances(Class<T> classType) {
        ApplicationModel applicationModel = ApplicationModelFactory.getCurrentApplication();
        List<T> instances = applicationModel.getAppExtensionLoader().getInstances(classType);
        for (T instance : instances) {
            recordOwner(classType, instance, applicationModel);

        }
        return instances;
    }

    /**
     * <pre>
     * 根据接口类型，返回所有的扩展服务实例
     * </pre>
     *
     * @param classType
     *            接口类型
     * @return
     */
    public static <T> List<T> getInstances(Class<T> classType,ApplicationModel applicationModel) {
        List<T> instances = applicationModel.getAppExtensionLoader().getInstances(classType);
        for (T instance : instances) {
            recordOwner(classType, instance, applicationModel);

        }
        return instances;
    }


    public static <T> T getInstance(Class<T> classType, String key) {
        ApplicationModel applicationModel = ApplicationModelFactory.getCurrentApplication();
        T instance = applicationModel.getAppExtensionLoader().getInstance(classType, key);
        if (instance != null) {
            recordOwner(classType, instance, applicationModel);
        }
        return instance;
    }


    public static ApplicationModel getApplicationModel(Object instance) {
        ApplicationModel applicationModel = INSTANCE_APP_MAP.get(instance);
        if (applicationModel == null) {
            throw new IllegalStateException("no owner app for " + instance.getClass());
        }
        return applicationModel;
    }

    private static <T> void recordOwner(Class<T> classType, T instance, ApplicationModel applicationModel) {
        // 不是shared类型的才进行记录

        if (!isSharedType(classType)) {
            ApplicationModel old = INSTANCE_APP_MAP.putIfAbsent(instance, applicationModel);
            if (old != null && old != applicationModel) {
                throw new IllegalStateException("App[" + applicationModel.getName() + "] and App[" + old.getName()
                        + "] share service " + classType.getName());
            }
        }
    }

    private static boolean isSharedType(Class<?> type) {

        boolean result = false;
        if (type != null && type.isAnnotationPresent(Shared.class)) {
            result = true;
        }

        return result;
    }

}
