package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.HSFThreadNameSpace</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class HSFThreadNameSpace {


    /**
     * <code>AddressProfiler</code>类启动的线程,在运行时对服务地址进行监控
     */
    public static final String ADDRESS_PROFILE = "HSF-AddressProfiler";

    /**
     * <code>HSFRuntimeInfoPublisherScheduler</code>类定义的线程,用来搜集HSF
     * Service的元数据和运行时数据，发送给Radius Server
     */
    public static final String HSF_RUNTIMEINFO_PUBLISHER = "HSF-RuntimeInfo-Publisher";

    /**
     * <code>NettyServer</code>类定义的线程,是Netty TCP服务的IO线程
     */
    public static final String HSF_NETTY_BOSS = "HSF-Boss";

    /**
     * <code>NettySharedHolder</code>类定义的线程,是Netty TCP服务的Worker线程
     */
    public static final String HSF_NETTY_WORKER = "HSF-Worker";

    /**
     * <code>ThreadPoolManager</code>类定义的线程池,是Netty TCP服务调度的默认业务处理线程
     */
    public static final String HSF_NETTY_PROCESSOR_DEFAULT = "HSFBizProcessor";

    /**
     * <code>ThreadPoolManager</code>类定义的线程池,service配置了线程池参数时的命名 <br>
     *
     * 格式如:HSF-ServerProcessor-com.taobao.hsf.jar.test.HelloWorldService:1.0.0.
     * kongming-9-thread-1<br>
     *
     * @param serviceUniqueName
     * @return
     */
    public static final String getNettyProcessorService(String serviceUniqueName) {
        return "HSFBizProcessor-" + serviceUniqueName;
    }

    /**
     * <code>NettySharedHolder</code>
     * 定义的线程池,包括consumer和provider的connection检测,以及consumer端的周期性发送心跳包
     */
    public static final String HSF_REMOTING_TIMER = "HSF-Remoting-Timer";

    /**
     * <code>AddressPool</code>类启动的线程,用来定时刷新可调用的服务地址
     * 规则:用单元化名字来区分不同的地址刷新线程，如果单元化名字为空，则用Default表示
     *
     * @param unit
     *            单元化名称
     * @return
     */
    public static final String getUnitThreadPoolName(String unit) {
        if (unit == null || "".equals(unit)) {
            return "HSF-AddressPool-Default";
        } else {
            return "HSF-AddressPool-" + unit;
        }
    }

    /**
     * <code>CallbackInvokeComponent.CallExecutorResourceHolder</code>
     * 类中定义的线程池,用来执行consumer端回调业务的执行
     */
    public final static String HSF_CONSUMER_INVOKE_CALLBACK = "HSF-CallBack";

}
