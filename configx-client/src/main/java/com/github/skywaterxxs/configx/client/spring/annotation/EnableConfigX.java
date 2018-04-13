package com.github.skywaterxxs.configx.client.spring.annotation;

import com.github.skywaterxxs.configx.client.spring.ConfigXConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xuxiaoshuo 2018/4/5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ConfigXConfigurationSelector.class)
public @interface EnableConfigX {
}