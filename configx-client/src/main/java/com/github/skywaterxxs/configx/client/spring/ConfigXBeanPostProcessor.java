package com.github.skywaterxxs.configx.client.spring;

import com.github.skywaterxxs.common.JsonUtil;
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


/**
 * @author xuxiaoshuo 2018/4/5
 */
public class ConfigXBeanPostProcessor implements BeanPostProcessor, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ConfigXBeanPostProcessor.class);


    private ConfigXStore configXStore = new ConfigXStore();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        setFields(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void setFields(Object bean, String beanName) {
        if (bean == null) {
            return;
        }
        Class<?> clazz = bean.getClass();
        setFields(bean, clazz, beanName);
        while (clazz.getSuperclass() != null && !Object.class.equals(clazz.getSuperclass())) {
            clazz = clazz.getSuperclass();
            setFields(bean, clazz, beanName);
        }
    }

    private void setFields(Object bean, Class<?> clazz, String beanName) {
        if (clazz == null) {
            return;
        }

        boolean isConfigBean = false;

        ConfigX configXAnnotation = AnnotationUtils.getAnnotation(clazz, ConfigX.class);
        if (configXAnnotation != null) {
            isConfigBean = true;
        }


        for (Field field : clazz.getDeclaredFields()) {

            ConfigX configXField = field.getAnnotation(ConfigX.class);
            if (!isConfigBean && configXField == null) {
                continue;
            }

            try {

                Class<?> fieldType = field.getType();

                ConfigMetaInfo configMetaInfo = new ConfigMetaInfo(beanName, field.getName());

                String configValue = configXStore.getConfigValue(configMetaInfo.getConfigKey());

                configMetaInfo.setValue(configValue);

                if (fieldType.equals(String.class)) {

                    invokeSetField(field, bean, configValue);
                    logger.info("成功设置{}", JsonUtil.toJson(configMetaInfo));
                }

                ConfigXStore.store.put(configMetaInfo.getConfigKey(), configMetaInfo);

            } catch (Throwable e) {
                throw new RuntimeException("初始化字段失败. bean: " + bean + ", field: " + field, e);
            }
        }
    }


    private void invokeSetField(Field field, Object bean, Object param) {
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, param);
    }

    @Override
    public void afterPropertiesSet() {
         configXStore.init();
    }
}
