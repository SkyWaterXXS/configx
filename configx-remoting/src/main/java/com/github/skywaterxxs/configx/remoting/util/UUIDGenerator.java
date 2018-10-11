package com.github.skywaterxxs.configx.remoting.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.util.UUIDGenerator</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class UUIDGenerator {
    private static AtomicLong opaque = new AtomicLong();

    public static final long getNextOpaque() {
        return opaque.getAndIncrement();
    }

}
