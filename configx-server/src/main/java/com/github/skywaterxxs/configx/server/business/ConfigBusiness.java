package com.github.skywaterxxs.configx.server.business;

import com.github.skywaterxxs.configx.server.controller.ConfigController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author xuxiaoshuo 2018/4/13
 */
@Component
public class ConfigBusiness {
    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    public String update() {

        return "更新是吧";
    }
}
