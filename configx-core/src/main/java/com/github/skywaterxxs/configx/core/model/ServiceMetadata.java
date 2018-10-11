package com.github.skywaterxxs.configx.core.model;

import com.github.skywaterxxs.configx.core.HSFConstants;
import com.github.skywaterxxs.configx.core.RemoteCallType;
import com.github.skywaterxxs.configx.remoting.EnumConfigStyle;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ServiceMetadata</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ServiceMetadata implements Serializable {
    private static final long serialVersionUID = 11L;

    public static final String DEFAULT_VERSION = "1.0.0"; // 默认版本号
    public static final String DEFAULT_GROUP = "HSF"; // 默认分组
    public static final String METHOD_TO_INJECT_CONSUMERIP_PROP_KEY = "METHOD_TO_INJECT_CONSUMERIP_PROP_KEY";
    public static final String METADATA_DELAYED_PUBLISH = "METADATA_DELAYED_PUBLISH";
    public static final String VALUE_FALSE = "false";
    public static final String VALUE_TRUE = "true";
    public final Object syncLock = new Object();
    private final AtomicBoolean needNotify = new AtomicBoolean(false); // 初始化时，判断是否需要唤醒

    private final boolean isProvider;
    private final boolean isTOP;
    private final boolean isNDI;

    private String interfaceName = "";
    private Class<?> ifClazz = null;
    private String version = DEFAULT_VERSION;
    private String dubboVersion = DEFAULT_VERSION;
    private String defaultGroup = DEFAULT_GROUP;
    private volatile String group = DEFAULT_GROUP;
    private volatile boolean isGroupChanged = false;
    private AtomicBoolean readyToPublish = new AtomicBoolean(true);
    private String name;
    private String desc;

    private volatile int maxWaitTimeForCsAddress = -1;
    private transient CountDownLatch csAddressCountDownLatch = new CountDownLatch(1);

    // 单元化部署
    public static final String WRITE_MODE_UNIT = "unit";
    public static final String WRITE_MODE_CENTER = "center";
    private String writeMode;
    private int route = -1;
    private boolean routeCheck = true;

    /**
     * 该服务，以及该服务调用的服务是否支持txc事务
     */
    private boolean enableTXC = false;

    private String generic;

    // filter support for dubbo
    private String filter;

    // retries support for dubbo
    private int retries = 0;

    // get default value from configuration
    private String proxyStyle = "jdk";
    private EnumConfigStyle configStyle = EnumConfigStyle.HSF;

    private int connectionNum = 1;
    private final transient AtomicLong connectionIndex = new AtomicLong(0);

    private int timeout = 3000;

    /**
     * callback方式调用，用户配置的处理器，支持上下文参数传递，区别于callbackhanlder只是用于reliable， reliablecallback方式。
     */

    private Object callbackInvoker;

    /**
     * 支持自定义回调方法的后缀
     */
    private String callbackMethodSuffix = "_callback";

    private final Map<String, AsyncallMethod> asyncallMethods = new HashMap<String, AsyncallMethod>();
    private final Map<String, MethodSpecial> methodSpecialMap = new HashMap<String, MethodSpecial>();
    private final Map<String, String> serviceProps = new HashMap<String, String>();

    /**
     * 业务线程池大小
     */
    private int consumerMaxPoolSize = 0;
    private AtomicInteger curConsumerMaxPoolSize = null;

    /**
     * 服务端线程池大小
     */
    private int corePoolSize = 0;
    private int maxPoolSize = 0;

    private transient Object target;

    private volatile transient ClassLoader servicePojoClassLoader;

    private volatile transient String uniqueServiceName;

    private volatile transient String uniqueDubboServiceName;

    private boolean isSupportEcho = false;

    // 安全验证的key，对于某些敏感应用需要配置这个值
    private String secureKey;

    // configserver集群站点，用于多注册
    private List<String> configserverCenter;

    //Consumer端使用，兼容Dubbo的广播调用
    private boolean broadcast = false;

    private String consistent = Boolean.FALSE.toString();

    // Public Method ----------------------------------------------
    public ServiceMetadata(final boolean isProvider, final boolean isTop, final boolean isNDI) {
        this.isProvider = isProvider;
        this.isTOP = isTop;
        this.isNDI = isNDI;
    }

    public ServiceMetadata(final boolean isProvider, final boolean isTop) {
        this(isProvider, isTop, false);
    }

    public ServiceMetadata(final boolean isProvider) {
        this(isProvider, false);
    }

    /*
     * 为了从字节流中反序列化时需要
     */
    public ServiceMetadata() {
        this(false, false);
    }

    public boolean isEnableTXC() {
        return enableTXC;
    }

    public void setEnableTXC(boolean enableTXC) {
        this.enableTXC = enableTXC;
    }


    /**
     * 增加需要异步调用的方法<br>
     * method的格式为：name:方法名;type:调用方式;listener:回调listener类名<br>
     */
    public void addAsyncallMethod(AsyncallMethod asyncFuncDesc) {
        asyncallMethods.put(asyncFuncDesc.getName(), asyncFuncDesc);
    }

    public boolean isProvider() {
        return isProvider;
    }

    public void addProperty(String propName, String propValue) {
        if ("_TIMEOUT".equalsIgnoreCase(propName)) {
            timeout = Integer.valueOf(propValue);
        }

        serviceProps.put(propName, propValue);
    }

    public int getTimeout() {
        return timeout;
    }

    public void removeProperty(String propName) {
        serviceProps.remove(propName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
//        result = prime * result + ((configStyle == null) ? 0 : configStyle.hashCode());
        result = prime * result + ((interfaceName == null) ? 0 : interfaceName.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceMetadata other = (ServiceMetadata) obj;
//        if (configStyle != other.configStyle)
//            return false;
        if (interfaceName == null) {
            if (other.interfaceName != null)
                return false;
        } else if (!interfaceName.equals(other.interfaceName))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    public boolean isTOP() {
        return isTOP;
    }

    public boolean isNDI() {
        return isNDI;
    }

    /**
     * @return 获取异步调用方法的对象
     */
    public AsyncallMethod getAsyncallMethod(String method) {
        if (asyncallMethods.isEmpty()) {
            return null;
        }

        return asyncallMethods.get(method.toLowerCase());
    }


    public String getCallbackMethodSuffix() {
        return callbackMethodSuffix;
    }

    public String getProxyStyle() {
        return proxyStyle;
    }

    public void setProxyStyle(String proxyStyle) {
        this.proxyStyle = proxyStyle;
    }

    public int getConnectionIndex() {
        if (connectionNum == 1) {
            return 0;
        }
        return (int) (connectionIndex.getAndIncrement() % connectionNum);
    }

    public boolean isReadyToPublish() {
        return readyToPublish.get();
    }

    public boolean setReadyToPublish(boolean readyToPublish) {
        return this.readyToPublish.compareAndSet(!readyToPublish, readyToPublish);
    }

    /**
     * @return 客户端线程池大小
     */
    public int getConsumerMaxPoolSize() {
        return consumerMaxPoolSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * @return 当前运行中的线程池大小
     */
    public AtomicInteger getCurConsumerMaxPoolSize() {
        return curConsumerMaxPoolSize;
    }

    /**
     * 获取描述信息
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 获取所属的Group
     */
    public String getGroup() {
        if (null == group || "".equals(group)) {
            return DEFAULT_GROUP;
        }
        return group;
    }

    public Class<?> getIfClazz() {
        return ifClazz;
    }

    /**
     * 获取接口名
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public MethodSpecial getMethodSpecial(String methodName) {
        if (methodSpecialMap.isEmpty()) {
            return null;
        }

        return methodSpecialMap.get(methodName);
    }

    /**
     * 获取简短名称
     */
    public String getName() {
        return name;
    }

    public String getProperty(String propName) {
        return (String) serviceProps.get(propName);
    }

    /**
     * 获取服务属性
     */
    public Map<String, String> getServiceProperties() {
        return serviceProps;
    }

    /**
     * 获取真实的作为服务的object
     */
    public Object getTarget() {
        return target;
    }

    public void initUniqueName() {
        this.uniqueServiceName = interfaceName + ":" + version;
        initDubboUniqueName();
    }

    public void initDubboUniqueName() {
        this.uniqueDubboServiceName = interfaceName + ":" + dubboVersion;
    }

    public String getUniqueName() {
        return uniqueServiceName;
    }

    public String getUniqueDubboName() {
        return uniqueDubboServiceName;
    }

    /**
     * 获取版本信息
     */
    public String getVersion() {
        return version;
    }

    public String getDubboVersion() {
        return dubboVersion;
    }

    /**
     * 是否为异步调用
     */
    public boolean isAsyncall(String method) {
        return !asyncallMethods.isEmpty() && asyncallMethods.containsKey(method.toLowerCase());
    }

    /**
     * 是否使用延迟发布功能
     */
    public boolean isDelayedPublish() {
        return VALUE_TRUE.equals(getProperty(METADATA_DELAYED_PUBLISH));
    }

    public void setCallbackMethodSuffix(String callbackMethodSuffix) {
        this.callbackMethodSuffix = callbackMethodSuffix;
    }

    public void setConsumerMaxPoolSize(int consumerMaxPoolSize) {
        setCurConsumerMaxPoolSize(consumerMaxPoolSize);
        this.consumerMaxPoolSize = consumerMaxPoolSize;
    }

    public void setCorePoolSize(String corePoolSize) {
        if (corePoolSize != null) {
            try {
                this.corePoolSize = Integer.parseInt(corePoolSize.trim());
            } catch (NumberFormatException e) {
                illArgsException("Property corePoolSize must be an integer!");
            }
        }
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(String defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public void setIfClazz(Class<?> ifClazz) {
        this.ifClazz = ifClazz;
        this.servicePojoClassLoader = ifClazz.getClassLoader();
    }

    public void setInterfaceName(String _interfaceName) {
        interfaceName = _interfaceName;
    }

    public void setMaxPoolSize(String maxPoolSize) {
        if (maxPoolSize != null) {
            try {
                this.maxPoolSize = Integer.parseInt(maxPoolSize.trim());
            } catch (NumberFormatException e) {
                illArgsException("Property maxPoolSize must be an integer!");
            }
        }
    }

    public ClassLoader getServicePojoClassLoader() {
        return servicePojoClassLoader;
    }

    public void setMethodSpecials(MethodSpecial[] methodSpecials) {
        for (int i = 0; i < methodSpecials.length; i++) {
            methodSpecialMap.put(methodSpecials[i].getMethodName(), methodSpecials[i]);
        }
    }

    public Collection<MethodSpecial> getMethodSpecials() {
        return methodSpecialMap.values();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setVersion(String _version) {
        version = _version;
        checkVersion();
    }

    public void setDubboVersion(String _version) {
        dubboVersion = _version;
    }

    public Object getCallbackInvoker() {
        return callbackInvoker;
    }

    public EnumConfigStyle getConfigStyle() {
        return configStyle;
    }

    public void setCallbackInvoker(Object callbackInvoker) {
        this.callbackInvoker = callbackInvoker;
    }

    public void setConfigStyle(String configStyle) {
        if (configStyle == null || configStyle.isEmpty()) {
            return;
        }
        this.configStyle = EnumConfigStyle.valueOf(configStyle.toUpperCase());
    }

    /**
     * 将异步调用方法的对象信息转化为String
     */
    public String toAsyncallMethodString() {
        StringBuilder amethodString = new StringBuilder();
        for (AsyncallMethod amethod : asyncallMethods.values()) {
            amethodString.append(amethod.toString());
            amethodString.append("&");
        }
        return amethodString.toString();
    }

    @Override
    public String toString() {
        return "ServiceName : " + getUniqueName() + ",Group : " + getGroup() + ".";
    }

    private void checkVersion() {
        if (version == null || "".equals(version.trim()) || "null".equalsIgnoreCase(version)) {
            illArgsException("ServiceMetadata.version=" + version);
        }
    }

    private void illArgsException(String msg) {
        throw new IllegalArgumentException(msg);
    }

    private void setCurConsumerMaxPoolSize(int poolsize) {
        if (curConsumerMaxPoolSize != null) {
            int currentConsumerCount = this.consumerMaxPoolSize - curConsumerMaxPoolSize.get();
            curConsumerMaxPoolSize.set(poolsize - currentConsumerCount);
        } else {
            curConsumerMaxPoolSize = new AtomicInteger(poolsize);
        }
    }

    public int getConnectionNum() {
        return connectionNum;
    }

    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }

    public void setFilter(String filter) {
        // filter support for dubbo
        this.filter = filter;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setSupportEcho(boolean value) {
        this.isSupportEcho = value;
    }

    public boolean isSupportEcho() {
        return isSupportEcho;
    }

    public void setRetries(int retries) {
        if (retries >= 0) {
            this.retries = retries;
        }
    }

    public int getRetries(String methodName) {
        MethodSpecial methodSpecial = this.getMethodSpecial(methodName);
        if (methodSpecial != null) {
            return methodSpecial.getRetries();
        } else {
            return retries;
        }
    }

    public AtomicBoolean getNeedNotify() {
        return needNotify;
    }

    public String getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(String writeMode) {
        this.writeMode = writeMode;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public boolean getRouteCheck() {
        return routeCheck;
    }

    public void setRouteCheck(boolean routeCheck) {
        this.routeCheck = routeCheck;
    }

//    public void fireMetadataAfterChanged() {
//        EventDispatcher.fireEvent(new Event(EventType.MetaDataChanged, this));
//    }

//    public void fireMetadataBeforeChanged() {
//        EventDispatcher.fireEvent(new Event(EventType.MetaDataBeforeChanged, this));
//    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public List<String> getConfigserverCenter() {
        return configserverCenter;
    }

    public void setConfigserverCenter(List<String> configserverCenter) {
        this.configserverCenter = configserverCenter;
    }

    /**
     * 同步等待cs的最大时间
     */
    public void setMaxWaitTimeForCsAddress(int timeout) {
        this.maxWaitTimeForCsAddress = timeout;
    }

    public int getMaxWaitTimeForCsAddress() {
        return this.maxWaitTimeForCsAddress;
    }

    public CountDownLatch getCsAddressCountDownLatch() {
        return csAddressCountDownLatch;
    }

    public boolean isCallBackMethod(String methodName) {
        if (asyncallMethods.isEmpty()) {
            return false;
        }

        AsyncallMethod aMethod = this.getAsyncallMethod(methodName);
        return aMethod == null ? false : HSFConstants.INVOKE_TYPE_CALL_BACK.equals(aMethod.getType());
    }

    public boolean isFutureMethod(String methodName) {
        if (asyncallMethods.isEmpty()) {
            return false;
        }

        AsyncallMethod aMethod = this.getAsyncallMethod(methodName);
        return aMethod == null ? false : HSFConstants.INVOKE_TYPE_FUTURE.equals(aMethod.getType());
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
    public boolean isReliableMethod(String methodName) {
        if (asyncallMethods.isEmpty()) {
            return false;
        }

        AsyncallMethod aMethod = this.getAsyncallMethod(methodName);
        return aMethod == null ? false : HSFConstants.INVOKE_TYPE_RELIABLE.equals(aMethod.getType());
    }

    /**
     * 判断是否存在可靠的异步调用
     */
    public boolean isExistReliableCall() {
        for (AsyncallMethod amethod : asyncallMethods.values()) {
            if (amethod.isReliable()) {
                return true;
            }
        }
        return false;
    }

    public boolean isReliableCallback() {
        for (AsyncallMethod amethod : asyncallMethods.values()) {
            if (amethod.isReliableCallback()) {
                return true;
            }
        }
        return false;
    }

    public String getConsistent() {
        return consistent;
    }

    public void setConsistent(String consistent) {
        this.consistent = consistent;
    }

    public void changeGroup(String newGroup) {
        this.setGroup(newGroup);
        this.isGroupChanged = true;
    }

    public void recoverGroupFlag() {
        this.isGroupChanged = false;
    }

    public boolean isGroupChanged() {
        return this.isGroupChanged;
    }

    /**
     * 描述：异步调用的方法的类
     */
    public class AsyncallMethod implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name; // 方法名
        private Method method;
        private Class<?> returnType;
        private String type; // 异步调用的类型
        private String callback; // 回调的callback类名
        private Object callbackInstance;

        private boolean isReliableCallback;

        public String getCallback() {
            return callback;
        }

        public Object getCallbackInstance() {
            return callbackInstance;
        }

        /**
         * reliable时，是否需要回调
         */
        public boolean isReliableCallback() {
            return this.isReliableCallback;
        }

        public Method getMethod() {
            return method;
        }

        public String getName() {
            return name;
        }

        public void setReliableCallback(boolean isReliableCallback) {
            this.isReliableCallback = isReliableCallback;
        }

        /**
         * 是否为可靠的单向异步调用
         */
        public boolean isReliable() {
            return "reliable".equalsIgnoreCase(type);
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        public String getType() {
            return type.toUpperCase();
        }

        /**
         * 是否为需要回调的异步调用
         */
        public boolean isCallback() {
            return RemoteCallType.CALLBACK.getCallType().equalsIgnoreCase(type);
        }

        /**
         * 是否为需要拿到future对象的异步调用
         */
        public boolean isFuture() {
            return RemoteCallType.FUTURE.getCallType().equalsIgnoreCase(type);
        }

        public void setCallback(String callback) {
            this.callback = callback;
        }

        public void setCallbackInstance(Object _callbackInstance) {
            callbackInstance = _callbackInstance;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public void setName(String name) {
            this.name = name.toLowerCase();
        }

        public void setReturnType(Class<?> returnType) {
            this.returnType = returnType;
        }

        public void setType(String type) {
            this.type = type;
        }

        /**
         * 转化为可识别的String
         */
        public String toString() {
            StringBuilder strBuilder = new StringBuilder("name:");
            strBuilder.append(name);
            strBuilder.append(";type:");
            strBuilder.append(type);
            strBuilder.append(";listener:");
            strBuilder.append(callback);
            return strBuilder.toString();
        }
    }

}

