package com.github.skywaterxxs.configx.server.business;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.client.domain.Chat;
import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import com.github.skywaterxxs.configx.remoting.ServerProcessor;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.server.business.ServerProcessorImpl</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/27 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ServerProcessorImpl implements ServerProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ServerProcessorImpl.class);

    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    // creating the ThreadPoolExecutor
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            threadFactory);

    @Override
    public Executor produceExecutor() {
        return executorPool;
    }

    @Override
    public void execute(Object message, Channel channel, long dispatchTime) {

        try {
            Chat inChat = JsonUtil.of((String) message, Chat.class);

            if (inChat == null || StringUtils.isBlank(inChat.getCommand())) {
                throw new RuntimeException("参数不全");
            }

            logger.info("command={}", inChat.getCommand());
            if (Objects.equals("getConfig", inChat.getCommand())) {

                Map<String, String> configs = new HashMap<>();

                configs.put("configXService#name", "XXS_test");

                Chat outChat = new Chat(inChat.getChatId(), "allConfig", configs);

                channel.writeAndFlush(JsonUtil.toJson(outChat));
            }


        } catch (Exception e) {
            logger.error("不支持的消息类型:message={}", message, e);
        }

    }
}
