package com.github.skywaterxxs.configx.remoting;

//import com.github.skywaterxxs.configx.remoting.util.PojoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.RemotingRuntimeInfoHolder</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RemotingRuntimeInfoHolder {
    private final LongAdder countSendRequests = new LongAdder();
    private final LongAdder countReceivedResponses = new LongAdder();
    private final LongAdder countProcessRequests = new LongAdder();
    private final LongAdder countConnectionsAsClient = new LongAdder();
    private final LongAdder countConnectionsAsServer = new LongAdder();

    private volatile boolean countService = false;
    private volatile String uniqueServiceName;
    private volatile int scount = 1;
    private volatile List<RemotingRuntimeInfoModel> slist = new ArrayList<RemotingRuntimeInfoModel>();
    private Object slock = new Object();

    private volatile boolean countConsumer = false;
    private volatile String uniqueConsumerName;
    private volatile int ccount = 1;
    private volatile List<RemotingRuntimeInfoModel> clist = new ArrayList<RemotingRuntimeInfoModel>();
    private Object clock = new Object();

    private final static RemotingRuntimeInfoHolder instance = new RemotingRuntimeInfoHolder();

    private RemotingRuntimeInfoHolder() {
    }

    public static RemotingRuntimeInfoHolder getInstance() {
        return instance;
    }

    public String getInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("countSendRequests:").append(RemotingRuntimeInfoHolder.getInstance().getcountSendRequests())
                .append(",\n countReceivedResponses:")
                .append(RemotingRuntimeInfoHolder.getInstance().getcountReceivedResponses())
                .append(",\n countProcessRequests:")
                .append(RemotingRuntimeInfoHolder.getInstance().getcountProcessRequests())
                .append(",\n countConnectionsAsClient:")
                .append(RemotingRuntimeInfoHolder.getInstance().getCountConnectionsAsClient())
                .append(",\n countConnectionsAsServer:")
                .append(RemotingRuntimeInfoHolder.getInstance().getCountConnectionsAsServer());
        return builder.toString();
    }

    public long getcountSendRequests() {
        return countSendRequests.longValue();
    }

    public void increasecountSendRequests() {
        this.countSendRequests.increment();
    }

    public long getcountReceivedResponses() {
        return countReceivedResponses.longValue();
    }

    public void increasecountReceivedResponses() {
        this.countReceivedResponses.increment();
    }

    public long getcountProcessRequests() {
        return countProcessRequests.longValue();
    }

    public long getCountConnectionsAsClient() {
        return countConnectionsAsClient.longValue();
    }

    public long getCountConnectionsAsServer() {
        return countConnectionsAsServer.longValue();
    }

    public void increaseCountConnectionsAsClient() {
        countConnectionsAsClient.increment();
    }

    public void increaseCountConnectionsAsServer() {
        countConnectionsAsServer.increment();
    }

    public void decreaseCountConnectionsAsClient() {
        countConnectionsAsClient.decrement();
    }

    public void decreaseCountConnectionsAsServer() {
        countConnectionsAsServer.decrement();
    }

    public void increasecountProcessRequests() {
        this.countProcessRequests.decrement();
    }

    public boolean isCountService() {
        return countService;
    }

    public void setCountService(boolean countService) {
        this.countService = countService;
    }

    public boolean isCountConsumer() {
        return countConsumer;
    }

    public void setCountConsumer(boolean countConsumer) {
        this.countConsumer = countConsumer;
    }

    public void addConsumer(String methodName, String consumerName, Object[] methodArgs, Object result, long time) {
//        if (Objects.equals(consumerName, uniqueConsumerName)) {
//            RemotingRuntimeInfoModel m = new RemotingRuntimeInfoModel();
//            m.setMethodName(methodName);
//            m.setInParams(PojoUtils.generalize(methodArgs));
//            m.setResult(PojoUtils.generalize(result));
//            m.setTime(time);
//            clist.add(m);
//        }
//        if (clist.size() >= ccount) {
//            synchronized (clock) {
//                countConsumer = false;
//                clock.notifyAll();
//            }
//        }
    }

    public void startCountConsumer(String consumerName, int count) {
        try {
            synchronized (clock) {
                uniqueConsumerName = consumerName;
                clist.clear();
                if (count > 0) {
                    ccount = count;
                }
                countConsumer = true;
                clock.wait(60 * 1000L);
            }
        } catch (InterruptedException e) {
        }
    }

    public List<RemotingRuntimeInfoModel> getCountConsumer() {
        return clist;
    }

    public void endCountConsumer() {
        countConsumer = false;
        clist.clear();
        ccount = 1;
    }

    public void addService(String methodName, String serviceName, Object[] methodArgs, Object result, long time) {
//        if (Objects.equals(serviceName, uniqueServiceName)) {
//            RemotingRuntimeInfoModel m = new RemotingRuntimeInfoModel();
//            m.setMethodName(methodName);
//            m.setInParams(PojoUtils.generalize(methodArgs));
//            m.setResult(PojoUtils.generalize(result));
//            m.setTime(time);
//            slist.add(m);
//        }
//        if (slist.size() >= scount) {
//            synchronized (slock) {
//                countService = false;
//                slock.notifyAll();
//            }
//        }
    }

    public void startCountService(String serviceName, int count) {
        try {
            synchronized (slock) {
                uniqueServiceName = serviceName;
                slist.clear();
                if (count > 0) {
                    scount = count;
                }
                countService = true;
                slock.wait(60 * 1000L);
            }
        } catch (InterruptedException e) {
        }
    }

    public List<RemotingRuntimeInfoModel> getCountService() {
        return slist;
    }

    public void endCountService() {
        countService = false;
        slist.clear();
        scount = 1;
    }
}
