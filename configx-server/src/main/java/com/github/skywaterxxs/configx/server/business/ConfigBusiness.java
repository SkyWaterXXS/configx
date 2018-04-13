package com.github.skywaterxxs.configx.server.business;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.server.controller.ConfigController;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxiaoshuo 2018/4/13
 */
@Component
public class ConfigBusiness {
    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    public String update() {
        Integer retryTime = 3;
        Integer currentTime = 0;

        Map<String, String> configs = new HashMap<>();
        configs.put("name","666");

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        ResponseBody responseBody = null;

        while (true) {
            if (currentTime++ >= retryTime) {
                logger.error("configX无法从服务器获取到配置信息");
                break;
            }
            try {
                String requestContent = JsonUtil.toJson(configs);

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, requestContent);

                Request request = new Request.Builder()
                        .url("http://127.0.0.1:8080/configX/update/v1")
                        .post(body)
                        .addHeader("cache-control", "no-cache")
                        .build();

                response = client.newCall(request).execute();
                responseBody = response.body();
                if (responseBody != null) {
                    String result = responseBody.string();

                    logger.info("客户端返回:{}", result);

                    return result;
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

        return "更新是吧";
    }
}
