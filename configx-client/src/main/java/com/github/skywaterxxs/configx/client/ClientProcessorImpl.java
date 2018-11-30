package com.github.skywaterxxs.configx.client;

import com.github.skywaterxxs.common.JsonUtil;
import com.github.skywaterxxs.configx.client.domain.Chat;
import com.github.skywaterxxs.configx.client.store.ConfigXStore;
import com.github.skywaterxxs.configx.remoting.client.ClientProcessor;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.client.ClientProcessorImpl</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/27 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ClientProcessorImpl implements ClientProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ClientProcessorImpl.class);

    @Override
    public void processor(Channel channel, String message) {
        try {
            Chat inChat = JsonUtil.of(message, Chat.class);


            if (inChat == null || StringUtils.isBlank(inChat.getCommand())) {
                throw new RuntimeException("参数不全");
            }

            logger.info("command={}", inChat.getCommand());
            if (Objects.equals("allConfig", inChat.getCommand())) {

                ConfigXStore.configs = (Map<String, String>) inChat.getCommandContent();


                Chat outChat = new Chat(inChat.getChatId(), "updateConfigDone", ConfigXStore.configs);

                channel.writeAndFlush(JsonUtil.toJson(outChat));
            }


            System.out.println("client Get " + message);

            ConfigXStore.configs.put("configXService#name", "XXS_test");

            ConfigXStore.initConfigCountDownLatch.countDown();
        } catch (Exception e) {
            logger.error("不支持的消息类型:message={}", message, e);
        }
    }
}
