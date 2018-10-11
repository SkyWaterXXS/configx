package com.github.skywaterxxs.configx.core.model;

import com.github.skywaterxxs.configx.remoting.EnumConfigStyle;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ApplicationModel</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ApplicationModel {

//    private static final Logger LOGGER = LoggerInit.LOGGER;

    private final ClassLoader appContextClassLoader;

//    @PetiteInject(ComponentNames.RPC_CONTAINER)
    private RpcContainer rpcContainer;

    private volatile String name;

    private volatile boolean isMain = false;

    private final Thread loadThread;

    private final ConcurrentMap<String, ProviderServiceModel> providedServices = new ConcurrentHashMap<String, ProviderServiceModel>();
    private final ConcurrentMap<String, ConsumerServiceModel> consumedMetadatas = new ConcurrentHashMap<String, ConsumerServiceModel>();
    private final ConcurrentMap<Integer, ProviderMethodModel> providerCoders = new ConcurrentHashMap<Integer, ProviderMethodModel>();

    //
    private final ConcurrentMap<String, InvocationHandler> providerService2Invoker = new ConcurrentHashMap<String, InvocationHandler>();
    private final ConcurrentMap<String, InvocationHandler> consumerService2Invoker = new ConcurrentHashMap<String, InvocationHandler>();

    private volatile String revertCoders = "";
    private volatile boolean isRefresh = true;

    private volatile Object tpsRule = null;

    private AppExtensionLoader appExtensionLoader;

    public ApplicationModel(String name, ClassLoader appContextClassLoader) {
        this.name = name;
        this.appContextClassLoader = appContextClassLoader;
        loadThread = Thread.currentThread();
        appExtensionLoader = HSFExtensionLoader.createAppExtensionLoader(this);

    }

    public AppExtensionLoader getAppExtensionLoader() {
        return appExtensionLoader;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }

    public String getName() {
        if (name == null) {
            throw new IllegalStateException("name is null");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassLoader getAppContextClassLoader() {
        return appContextClassLoader;
    }

    public boolean isSupportTps() {
        return tpsRule != null;
    }

    public void setTpsRule(Object tpsRule) {
        this.tpsRule = tpsRule;
    }

    public Object getTpsRule() {
        return this.tpsRule;
    }

    public <T> T getBean(String name){
        if(rpcContainer == null)
            throw new IllegalStateException("RpcContainer of this Application is null.Cannot get any bean of this application.");
        return rpcContainer.getBean(name);
    }

    public void wire(Object bean){
        rpcContainer.wire(bean);
    }

    public ProviderServiceModel getProvidedServiceModel(String serviceName) {
        return providedServices.get(serviceName);
    }

    public InvocationHandler getServerInvocationHandler(String service) {
        return providerService2Invoker.get(service);
    }

    public InvocationHandler getClientInvocationHandler(String service) {
        return consumerService2Invoker.get(service);
    }

//    public String getProviderRevertCoders() {
//        if (isRefresh) {
//            Map<String, Integer> mapper = Maps.newHashMap();
//            for (ProviderServiceModel serviceModel : providedServices.values()) {
//                for (ProviderMethodModel methodModel : serviceModel.getAllMethods()) {
//                    mapper.put(methodModel.getMethodKeyWithServiceName(), methodModel.getIndex());
//                }
//            }
//            revertCoders = (String) PojoUtils.generalize(mapper);
//            isRefresh = false;
//        }
//        return revertCoders;
//    }

    public ConsumerServiceModel getConsumedServiceModel(String serviceName) {
        return consumedMetadatas.get(serviceName);
    }

    public Collection<ConsumerServiceModel> allConsumedServices() {
        return new ArrayList<ConsumerServiceModel>(consumedMetadatas.values());
    }

    public Collection<ProviderServiceModel> allProvidedServices() {
        return new ArrayList<ProviderServiceModel>(providedServices.values());
    }

    public boolean initConsumerService(String servicename, ConsumerServiceModel serviceModel) {
        // TODO thread safe?

        if (Thread.currentThread() != loadThread) {
//            LOGGER.warn("Init consumer service must be in the same thread:" + servicename + "|" + Thread.currentThread()
//                    + "|" + loadThread);
            // throw new RuntimeException("Init service must be in the same
            // thread");
        }

        if (consumedMetadatas.putIfAbsent(servicename, serviceModel) != null) {
//            LOGGER.warn("Already register the same:" + servicename);
            return false;
        }

//        consumerService2Invoker.put(servicename, HandlerChainBuilder.buildHandlerChain(serviceModel.getMetadata(),this));

        return true;
    }

    /**
     * make sure thread safe for init data
     *
     * @param servicename
     * @param serviceModel
     */
    public void initProviderService(String servicename, ProviderServiceModel serviceModel) {
        // TODO thread safe?
        if (Thread.currentThread() != loadThread) {
//            LOGGER.warn("Init provider service must be in the same thread:" + servicename + "|" + Thread.currentThread()
//                    + "|" + loadThread);
            // throw new RuntimeException("Init service must be in the same
            // thread");
        }

        if (this.providedServices.put(servicename, serviceModel) != null) {
//            LOGGER.warn("Already register the same:" + servicename);
        }
        for (ProviderMethodModel methodModel : serviceModel.getAllMethods()) {
            providerCoders.put(methodModel.getIndex(), methodModel);
        }
        this.isRefresh = true;

        ApplicationModelFactory.put(servicename, this);
        ApplicationModelFactory.put(servicename, serviceModel);

//        providerService2Invoker.put(servicename, HandlerChainBuilder.buildHandlerChain(serviceModel.getMetadata(),this));
    }

    public void deleteProviderService(String serviceName){
        ProviderServiceModel serviceModel = providedServices.remove(serviceName);
        if(serviceModel != null) {
            for (ProviderMethodModel methodModel : serviceModel.getAllMethods()) {
                providerCoders.remove(methodModel.getIndex());
            }
        }
        //TODO AppliactionFactory.remove(serviceName)
        providerService2Invoker.remove(serviceName);
    }

    public EnumConfigStyle getConfigStyleOfConsumer(String serviceUniqueName) {
        return this.consumedMetadatas.get(serviceUniqueName).getMetadata().getConfigStyle();
    }

    public static ApplicationModel instance() {
        // ClassLoader appLoader =
        // Thread.currentThread().getContextClassLoader();
        // if(appLoader == null){
        // throw new RuntimeException("not app loader found");
        // }
        // ApplicationModel app = loader2app.get(appLoader);
        // if(app == null){
        // loader2app.putIfAbsent(appLoader, new ApplicationModel(appLoader));
        // app = loader2app.get(appLoader);
        // }
        // return app;
        return ApplicationModelFactory.getCurrentApplication();
    }

    public RpcContainer getRpcContainer(){
        return rpcContainer;
    }
}