package com.github.skywaterxxs.configx.test;

import com.github.skywaterxxs.configx.client.spring.annotation.EnableConfigX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxiaoshuo
 */
@EnableConfigX
@SpringBootApplication
public class ConfigXTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigXTestApplication.class, args);
    }
}
