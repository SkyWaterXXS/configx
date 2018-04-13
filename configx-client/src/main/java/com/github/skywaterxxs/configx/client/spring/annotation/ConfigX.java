package com.github.skywaterxxs.configx.client.spring.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author xuxiaoshuo 2018/4/5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface ConfigX {
}
