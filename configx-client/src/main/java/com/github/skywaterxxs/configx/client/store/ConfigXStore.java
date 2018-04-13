package com.github.skywaterxxs.configx.client.store;

import com.github.skywaterxxs.common.JsonUtil;
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

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"name\":\"sd\"}");

                Request request = new Request.Builder()
                        .url("http://127.0.0.1:5210/config/init/v1")
                        .post(body)
                        .addHeader("cache-control", "no-cache")
                        .build();

                response = client.newCall(request).execute();
                responseBody = response.body();
                if (responseBody != null) {
                    String result = responseBody.string();

                    configs = JsonUtil.ofMap(result, String.class, String.class);


                    break;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (responseBody != null) {
                    responseBody.close();
                }
                if (response != null) {
                    response.close();
                }
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
