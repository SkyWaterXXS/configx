package com.github.skywaterxxs.configx.client.store;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.client.chat.ChatClient;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author xuxiaoshuo 2018/4/10
 */
public class ConfigXStore {

    private static final Logger logger = LoggerFactory.getLogger(ConfigXStore.class);


    public static Map<String, Object> store = new Hashtable<>();

    private Map<String, String> configs = new HashMap<>();

    public void init() {

        if (!configs.isEmpty()) {
            return;
        }

        Integer retryTime = 3;
        Integer currentTime = 0;

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        ResponseBody responseBody = null;

        while (true) {
            if (currentTime++ >= retryTime) {
                logger.error("configX无法从服务器获取到配置信息");
                break;
            }
            try {

                ChatClient chatClient=new ChatClient();
                chatClient.connect("127.0.0.1", 8000);


            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    public String getConfigValue(String configKey) {
        return configs.get(configKey);
    }

    public Map<String, String> getAllConfig(String configKey) {
        return configs;
    }
}
