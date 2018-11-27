package com.github.skywaterxxs.configx.client.store;

import com.github.skywaterxxs.common.RetryRunUtil;
import com.github.skywaterxxs.configx.remoting.client.NettyClient;
import com.github.skywaterxxs.configx.remoting.client.RemotingURL;
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

    public static Map<String, NettyClient> clientStore = new Hashtable<>();


    public static Map<String, Object> store = new Hashtable<>();

    private Map<String, String> configs = new HashMap<>();

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

                NettyClient a = nettyClient.createClient(remotingURL);

                a.getChannel().writeAndFlush("我来了");
                clientStore.put(remotingURL.getHost(), a);
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
