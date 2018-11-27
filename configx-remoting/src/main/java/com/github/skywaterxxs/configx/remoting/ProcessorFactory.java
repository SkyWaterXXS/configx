package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.ProcessorFactory</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/4 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProcessorFactory {

    public static ServerProcessor serverProcessor;

    public static ServerProcessor getProcessor(Object message) {
        return serverProcessor;
    }



}
