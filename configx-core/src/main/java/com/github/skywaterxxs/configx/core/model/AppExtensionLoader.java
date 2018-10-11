package com.github.skywaterxxs.configx.core.model;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.AppExtensionLoader</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class AppExtensionLoader {

//    private static final Logger LOGGER = LoggerInit.LOGGER;
    // 所有keyName
    private static final String ALL_NAME = "%%";
    // 扩展接口到实现类之间的关系，{SPIClassType:
    // {ImplementsClassName:ImplementsClassInstance}}
    private ConcurrentHashMap<Class<?>, Map<String, Class<?>>> spiRepository;
    // 单例缓存，{ImplementsClassInstance: ObjectInstance}
    private ConcurrentHashMap<Class<?>, Object> singletonCache;
    // 父容器
    private AppExtensionLoader parent;
    // 当前类加载器
    private ClassLoader loader;
    // 当前应用
    private ApplicationModel application;

    AppExtensionLoader(ApplicationModel applicationModel, AppExtensionLoader parent) {
        spiRepository = new ConcurrentHashMap<Class<?>, Map<String, Class<?>>>();
        singletonCache = new ConcurrentHashMap<Class<?>, Object>();
        this.application = applicationModel;
        this.loader = AppExtensionLoader.class.getClassLoader();
        this.parent = parent;
    }

    AppExtensionLoader() {
        this(null, null);
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
    public <T> T getInstance(Class<T> classType) {
        return getInstance(classType, ALL_NAME);
    }

    /**
     * <pre>
     * 根据接口类型，返回所有的扩展服务实例
     * </pre>
     *
     * @param classType
     *            接口类型
     * @return 返回所有的接口对应的扩展实例
     */
    public <T> List<T> getInstances(Class<T> classType) {
        List<T> instanceList = Collections.emptyList();
        if (parent != null && isSharedType(classType)) {
            instanceList = parent.getInstances(classType);
        }
        // parent没有找到，那就本地找
        if (instanceList == null || instanceList.isEmpty()) {
            instanceList = findInstances(classType, ALL_NAME);
        }

        return instanceList;
    }

    /**
     * <pre>
     * 根据接口类型以及name属性，返回指定的扩展服务实例
     *
     * 其中name属性由 <code>@com.taobao.hsf.annotation.Name("name")</code>注释在扩展实现类型上
     * </pre>
     *
     * @param classType
     *            接口类型
     * @param name
     *            指定扩展名称
     * @return 返回指定的接口对应的扩展实例
     */
    public <T> T getInstance(Class<T> classType, String name) {
        T instance = null;
        if (parent != null && isSharedType(classType)) {
            instance = parent.getInstance(classType, name);
        }
        // parent没有找到，那就本地找
        if (instance == null) {
            List<T> instanceList = findInstances(classType, name);
            if (instanceList != null && !instanceList.isEmpty()) {
                instance = instanceList.get(0);
            }
        }

        return instance;
    }

    /**
     * 根据name获取对应的扩展，如果name为全部，则返回所有的扩展实例
     *
     * @param classType
     * @param name
     *            扩展命名
     * @return
     */
    private final <T> List<T> findInstances(Class<T> classType, String name) {
        if (classType != null) {
            loadSpi(classType);
        }

        List<T> instanceList = new ArrayList<T>();
        Map<String, Class<?>> spiTypes = spiRepository.get(classType);
        if (spiTypes != null && !spiTypes.isEmpty()) {
            // 单例或者多例的处理
            if (isSingletonType(classType)) {
                if (ALL_NAME.equals(name)) {
                    for (Class<?> clazz : spiTypes.values()) {
                        Object instance = singletonCache.get(clazz);
                        if (instance != null) {
                            instanceList.add((T) instance);
                        }
                    }
                } else {
                    Class<?> clazz = spiTypes.get(name);
                    if (clazz == null) {
//                        LOGGER.warn("AppExtensionLoader", "",
//                                "can not find spi-interface:{}'s provider class with Name:{}",
//                                new Object[] { classType.getName(), name });
                    } else {
                        instanceList.add((T) singletonCache.get(clazz));
                    }
                }
            } else {
                if (ALL_NAME.equals(name)) {
                    for (Class<?> clazz : new HashSet<Class<?>>(spiTypes.values())) {
                        Object spiInstance = ServiceLoaderUtils.newSpiInstance(clazz);
                        instanceList.add((T) spiInstance);
                    }
                } else {
                    Class<?> clazz = spiTypes.get(name);
                    if (clazz == null) {
//                        LOGGER.warn("AppExtensionLoader", "",
//                                "can not find spi-interface:{}'s provider class with Name:{}",
//                                new Object[] { classType.getName(), name });
                    } else {
                        Object spiInstance = ServiceLoaderUtils.newSpiInstance(clazz);
                        instanceList.add((T) spiInstance);
                    }
                }
            }
        }

        return instanceList;
    }

    /**
     * 加载当前type对应的所有SPI类型信息，同时进行单例的初始化
     *
     * @param type
     */
    private final synchronized void loadSpi(Class<?> type) {
        if (!spiRepository.containsKey(type)) {
            Map<String, Class<?>> spiImpls = new HashMap<String, Class<?>>();
            // 由应用加载，但是排除com.taobao.hsf的开头
            if (application != null && application.getAppContextClassLoader() != null) {
                fillSpiImpls(type, spiImpls,
                        ServiceLoaderUtils.load(type, application.getAppContextClassLoader(), "com.taobao.hsf"));
            }

            fillSpiImpls(type, spiImpls, ServiceLoaderUtils.load(type, loader));

            if (isSingletonType(type)) {
                for (Class<?> clazz : new HashSet<Class<?>>(spiImpls.values())) {
                    if (!singletonCache.containsKey(clazz)) {
                        Object spiInstance = ServiceLoaderUtils.newSpiInstance(clazz);
                        singletonCache.put(clazz, spiInstance);
                    }
                }
            }

            spiRepository.put(type, spiImpls);
        }
    }

    /**
     * <pre>
     * 是否单例类型
     * 没有标注单例类型的，也认为是单例
     * </pre>
     *
     * @param type
     * @return
     */
    private static final boolean isSingletonType(Class<?> type) {
        boolean result = false;
        if (type != null) {
            Scope.Option option = Scope.Option.SINGLETON;
            if (type.isAnnotationPresent(Scope.class)) {
                Scope scope = type.getAnnotation(Scope.class);
                option = scope.value();
            }
            result = (option == Scope.Option.SINGLETON);
        }

        return result;
    }

    /**
     * <pre>
     * 当前类型为共享类型
     *
     * </pre>
     *
     * @param type
     * @return
     */
    private static final boolean isSharedType(Class<?> type) {
        boolean result = false;
        if (type != null && type.isAnnotationPresent(Shared.class)) {
            result = true;
        }

        return result;
    }

    /**
     * <pre>
     * 将类型type的扩展从src拷贝到dest中
     *
     * </pre>
     *
     * @param type
     * @param dest
     * @param src
     */
    private static final void fillSpiImpls(Class<?> type, Map<String, Class<?>> dest, Map<String, Class<?>> src) {
        if (dest != null && src != null) {
            for (Map.Entry<String, Class<?>> entry : src.entrySet()) {
                // 把成功加载的扩展类型进行保存
                if (entry.getValue() != null) {
//                    if (LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("AppExtensionLoader", "found provider-class:{} for spi-interface:{}",
//                                new Object[] { entry.getKey(), type.getName() });
//                    }
                    dest.put(entry.getKey(), entry.getValue());
                    // 如果有keyname，那么增加一个别名
                    Name instanceKey = entry.getValue().getAnnotation(Name.class);
                    if (instanceKey != null && StringUtils.isNotBlank(instanceKey.value())) {
                        dest.put(instanceKey.value(), entry.getValue());
                    }
                }
            }
        }
    }

}

