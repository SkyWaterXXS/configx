package com.github.skywaterxxs.configx.test;

import com.github.skywaterxxs.configx.client.spring.annotation.ConfigX;

/**
 * @author xuxiaoshuo 2018/4/5
 */
@ConfigX
public class ConfigXService {
    private String name;

    public String getName() {
        return name;
    }
}
