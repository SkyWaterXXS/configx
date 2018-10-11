package com.github.skywaterxxs.configx.core.model;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ServiceLoaderUtils</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 用来完成 META-INF/services对应接口的扩展信息加载
 *
 * 原有的{@link java.util.ServiceLoader} 进行加载时会进行初始化，但是有些场景只需要类型信息，不需要初始化
 *
 * 相比较{@link java.util.ServiceLoader} 优势在于加载SPI后，如果类型初始化不成功，也能知道原因，而不是简单的失败
 * </pre>
 *
 * @author weipeng2k 2015年11月17日 下午1:47:56
 */
final class ServiceLoaderUtils {

//    private static final Logger LOGGER = LoggerInit.LOGGER;

    private static final String PREFIX = "META-INF/services/";

    /**
     * <pre>
     * 返回loader中对于type的SPI扩展类型信息，其中key为类型名称，value为类型实例
     * 如果加载类型实例错误，则key对应的value为空
     * </pre>
     *
     * @param type
     *            SPI接口信息
     * @param loader
     *            类加载器
     * @param excludePackages
     *            需要排除的包名
     * @return 如果没有扩展返回size为0的map，key为类型名称，value为类型实例
     */
    public static final Map<String, Class<?>> load(Class<?> type, ClassLoader loader, String... excludePackages) {
        if (type == null || loader == null) {
            throw new IllegalArgumentException("type and loader can not be null");
        }

        Map<String, Class<?>> repo = new HashMap<String, Class<?>>();

        try {
            Enumeration<URL> urlEnums = loader.getResources(PREFIX + type.getName());
            while (urlEnums.hasMoreElements()) {
                BufferedReader br = null;
                URL currentUrl = urlEnums.nextElement();
                try {
                    br = new BufferedReader(new InputStreamReader(currentUrl.openStream()));
                    String classNameStr = null;
                    while ((classNameStr = br.readLine()) != null) {
                        // 不包含#，不为空，不包含=的扩展形式
                        if (StringUtils.containsNone(classNameStr, "#") && StringUtils.isNotBlank(classNameStr) && StringUtils.containsNone(classNameStr, "=")) {
                            String className = classNameStr.trim();
                            boolean exclude = false;
                            if (excludePackages != null) {
                                for (String excludePack : excludePackages) {
                                    if (className.startsWith(excludePack)) {
                                        exclude = true;
                                        break;
                                    }
                                }
                            }

                            if (exclude) {
//                                LOGGER.debug("ServiceLoaderUtils",
//                                        "provider-class:{} for spi-interface:{} is exclude by package exclusion",
//                                        new Object[] { className, type.getName() });
                            } else {
                                // 先期将类型名称设置
                                repo.put(className, null);
                                try {
                                    Class<?> impl = loader.loadClass(className);
                                    if (type.isAssignableFrom(impl)) {
                                        repo.put(className, impl);
                                    } else {
//                                        LOGGER.warn("ServiceLoaderUtils",
//                                                "provider-class:{} is not implements spi-interface:{}",
//                                                new Object[] { className, type.getName() });
                                    }
                                } catch (Throwable ex) {
//                                    LOGGER.warn("ServiceLoaderUtils",
//                                            "provider-class:{} for spi-interface:{} got exception when loaded class, maybe it can not be found.",
//                                            new Object[] { className, type.getName() });
                                }
                            }
                        }
                    }
                } finally {
                    if (br != null) {
                        br.close();
                    }
                }
            }
        } catch (IOException ex) {
//            LOGGER.error("ServiceLoaderUtils", "", "parse spi-interface:" + type.getName() + " got exception.", ex);
            throw new RuntimeException(ex);
        }

        return repo;
    }

    /**
     * 根据类型信息创建一个SPI的实现
     *
     * @param clazz
     * @return
     */
    public static final <T> T newSpiInstance(Class<T> clazz) {
        T result = null;
        try {
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
                result = (T) clazz.newInstance();
            } finally {
                Thread.currentThread().setContextClassLoader(tccl);
            }
        } catch (Exception ex) {
//            LOGGER.error("ServiceLoaderUtils", "",
//                    "create provider-class:" + clazz.getName() + " instance got exception.", ex);
        }

        return result;
    }

}
