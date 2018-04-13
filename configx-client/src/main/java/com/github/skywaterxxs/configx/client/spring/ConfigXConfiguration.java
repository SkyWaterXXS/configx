package com.github.skywaterxxs.configx.client.spring;

import com.github.skywaterxxs.configx.client.spring.annotation.EnableConfigX;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * @author xuxiaoshuo 2018/4/5
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class ConfigXConfiguration implements ImportAware {

    protected AnnotationAttributes enableConfigX;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableConfigX = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableConfigX.class.getName(), false));
        if (this.enableConfigX == null) {
            throw new IllegalArgumentException(
                    "@EnableConfigX is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    public ConfigXBeanPostProcessor configXBeanPostProcessor() {
        Assert.notNull(this.enableConfigX, "@EnableConfigX annotation metadata was not injected");
        return new ConfigXBeanPostProcessor();
    }
}
