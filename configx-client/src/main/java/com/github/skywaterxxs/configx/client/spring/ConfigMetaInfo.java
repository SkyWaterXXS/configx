package com.github.skywaterxxs.configx.client.spring;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.client.spring.ConfigMetaInfo</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/27 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigMetaInfo {

    private String configKey;

    private String beanName;

    private String fieldName;

    private Object value;

    public ConfigMetaInfo(String beanName, String fieldName) {
        this.configKey =beanName+"#"+fieldName;
        this.beanName = beanName;
        this.fieldName = fieldName;
    }

    public ConfigMetaInfo(String beanName, String fieldName, Object value) {
        this.configKey =beanName+"#"+fieldName;
        this.beanName = beanName;
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
