package com.github.skywaterxxs.configx.client.spring;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.client.spring.annotation.ConfigX;
import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


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

    private void getAllInvokersOfABean(String beanName, Object bean)
    {
//        Map<String,MethodInfo> invokersOfABean = new HashMap<String/*MethodSignature*/,MethodInfo>();
        Class<?> c = bean.getClass();
        Class<?> originClass = AopUtils.getTargetClass(bean);

        logger.info("=====Scanning Bean named: "+beanName+" start");
        logger.info("Class: "+c.getCanonicalName()+" OriginClass: "+originClass.getCanonicalName());
        for(Method m :originClass.getMethods())
        {
            ConfigX iv = m.getAnnotation(ConfigX.class);
            if(iv!=null)
            {
//                MethodInfo mi = null;
                //存在代理类
                if(c!=originClass)
                {
                    //尝试获得代理类的对应方法，保持AOP的功能
                    try
                    {
                        Method proxyMethod = c.getMethod(m.getName(), m.getParameterTypes());
//                        mi = new MethodInfo(proxyMethod);
                    }
                    catch(Exception e)
                    {
                        logger.error("Exception generated when checking the counter method named: "+m.getName()+" from class named: "+c.getCanonicalName());
                        //存在代理类，但添加AteyeInvoker注释的方法没有被代理，直接采用Target（原始类）的Method
//                        mi = new MethodInfo(m);
                        //要用Target对象invoke
//                        mi.setTargetSpecial(true);
                    }
                }
                else
                {
                    //不存在代理类
//                    mi = new MethodInfo(m);
                }
//                if(iv.description()!=null&&!"".equals(iv.description()))
//                {
//                    mi.setDesc(iv.description());
//                }
//                if(iv.paraDesc()!=null&&!"".equals(iv.paraDesc()))
//                {
//                    mi.setParamDesc(iv.paraDesc());
//                }
//                mi.setType(iv.type()); //设置type值；
//                invokersOfABean.put(mi.getSignature(), mi);
//                logger.info("Monitored Method: "+mi.getSignature());
            }
            else
            {
                logger.info("Ordinary Method: "+m.getName());
            }
        }
        logger.info("=====Scanning Bean named: "+beanName+" end");
//        return invokersOfABean;
    }




    @Override
    public void afterPropertiesSet() {
         configXStore.init();
    }
}
