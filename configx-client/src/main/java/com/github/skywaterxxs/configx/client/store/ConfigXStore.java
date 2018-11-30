package com.github.skywaterxxs.configx.client.store;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.common.RetryRunUtil;
import com.github.skywaterxxs.configx.client.ClientProcessorImpl;
import com.github.skywaterxxs.configx.client.domain.Chat;
import com.github.skywaterxxs.configx.client.spring.ConfigMetaInfo;
import com.github.skywaterxxs.configx.remoting.client.NettyClient;
import com.github.skywaterxxs.configx.remoting.client.RemotingURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author xuxiaoshuo 2018/4/10
 */
public class ConfigXStore {
    public static CountDownLatch initConfigCountDownLatch = new CountDownLatch(1);

    ClientProcessorImpl messageProcessor = new ClientProcessorImpl();

    private static final Logger logger = LoggerFactory.getLogger(ConfigXStore.class);

    public static Map<String, NettyClient> clientStore = new Hashtable<>();


    public static Map<String, ConfigMetaInfo> store = new Hashtable<>();

    public static Map<String, String> configs = new HashMap<>();

    public void init() {

        if (!configs.isEmpty()) {
            return;
        }

        Integer retryTime = 3;

        RetryRunUtil.runRetry(() -> {
            try {
                NettyClient nettyClient = new NettyClient();

                RemotingURL remotingURL = new RemotingURL();
                remotingURL.setPort(7000);
                remotingURL.setHost("127.0.0.1");

                NettyClient a = nettyClient.createClient(remotingURL, messageProcessor);

                clientStore.put(remotingURL.getHost(), a);

                System.out.println("等待服务器响应");
                Chat outChat = new Chat( "getConfig");

                a.getChannel().writeAndFlush(JsonUtil.toJson(outChat));

                initConfigCountDownLatch.await();
                System.out.println("获取配置完成");

            } catch (Exception e) {
                throw new RuntimeException("need retry", e);
            }

        }, retryTime);


    }

    public String getConfigValue(String configKey) {
        return configs.get(configKey);
    }

    public Map<String, String> getAllConfig(String configKey) {
        return configs;
    }
}
