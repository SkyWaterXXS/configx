package com.github.skywaterxxs.configx.client.spring;

import com.github.skywaterxxs.configx.client.spring.annotation.ConfigX;
import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Properties;


/**
 * @author xuxiaoshuo 2018/4/5
 */
public class ConfigXBeanPostProcessor implements BeanPostProcessor,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ConfigXBeanPostProcessor.class);


    ConfigXStore configXStore = new ConfigXStore();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        setFields(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void setFields(Object bean) {
        if (bean == null) {
            return;
        }
        Class<?> clazz = bean.getClass();
        setFields(bean, clazz);
        while (clazz.getSuperclass() != null && !Object.class.equals(clazz.getSuperclass())) {
            clazz = clazz.getSuperclass();
            setFields(bean, clazz);
        }
    }

    private void setFields(Object bean, Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        ConfigX configXAnnotation = AnnotationUtils.getAnnotation(clazz, ConfigX.class);
        if (configXAnnotation == null) {
            return;
        }

        ConfigXStore.store.put("configXBean",bean);


        for (Field field : clazz.getDeclaredFields()) {
            try {
                Class<?> fieldType = field.getType();

                String configKey = field.getName();

                String configValue = configXStore.getConfigValue(configKey);

                if (fieldType.equals(Properties.class)) {
                    Properties properties = new Properties();
                    properties.putAll(null);
                    invokeSetField(field, bean, properties);
                    continue;
                }
                if (fieldType.equals(String.class)) {

                    invokeSetField(field, bean, configValue);
                    logger.info("成功设置{}",configKey);
                    continue;
                }

            }
            catch (Throwable e) {
                throw new RuntimeException("初始化字段失败. bean: " + bean + ", field: " + field, e);
            }
        }
    }


    private void invokeSetField(Field field, Object bean, Object param) {
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, param);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread a = new Thread(() -> {
            configXStore.init();

        });

//        Thread b = new Thread(() -> {
//            configXStore.init();
//
//        });

        a.start();
//        b.start();
    }
}
