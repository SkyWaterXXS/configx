package com.github.skywaterxxs.configx.core.processor;

import com.github.skywaterxxs.configx.remoting.Connection;
import com.github.skywaterxxs.configx.remoting.Processor;
import com.github.skywaterxxs.configx.remoting.RpcRequest;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.processor.RpcRequestProcessor</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/4 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RpcRequestProcessor implements Processor {
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    Executor executor = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            threadFactory);

    @Override
    public Executor produceExecutor() {
        return executor;
    }

    @Override
    public void execute(Object message, Channel channel, long dispatchTime){
        if (!(message instanceof RpcRequest)) {
            return;
        }
        RpcRequest rpcRequest = (RpcRequest) message;
//        rpcRequest

//        Method method = ReflectionUtils.findMethod(SpringContextUtil.getBean("testService").getClass(), "testRelect", String.class);

//        try {

//            Object obj = method.invoke(SpringContextUtil.getBean("testService"), "2222");


//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }


    }
}
