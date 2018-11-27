package com.github.skywaterxxs.configx.client.store;

import com.github.skywaterxxs.configx.remoting.client.MessageProcessor;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.client.store.MessageProcessorImpl</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/27 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class MessageProcessorImpl implements MessageProcessor {

    @Override
    public void processor(String message) {

        System.out.println("client Get "+message);

        ConfigXStore.configs.put("configXService#name","XXS_test");

        ConfigXStore.initConfigCountDownLatch.countDown();
    }
}
