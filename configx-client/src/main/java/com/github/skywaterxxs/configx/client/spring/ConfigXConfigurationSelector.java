package com.github.skywaterxxs.configx.client.spring;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author xuxiaoshuo 2018/4/5
 */
public class ConfigXConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {ConfigXConfiguration.class.getName(), ConfigXServletContextInitializer.class.getName()};
    }
}