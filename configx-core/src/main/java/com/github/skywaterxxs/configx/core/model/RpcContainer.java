package com.github.skywaterxxs.configx.core.model;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.RpcContainer</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RpcContainer {

    private AtomicBoolean containerInited = new AtomicBoolean(false);
//    private PetiteConfig containerConfig = new PetiteConfig();

//    private static volatile MarkedClassesResult markedClassesResult;
//    private static PetiteContainer parentContainer;
    private static AtomicBoolean parentContainerStarted = new AtomicBoolean(false);

//    private PetiteContainer prototypeContainer;

    public RpcContainer() {
    }

    public void shutdown() {

//        prototypeContainer.shutdown();
    }

    public void shutdownTotally() {

//        parentContainer.shutdown();
    }

    public void init() {
//        if (markedClassesResult == null)
//            markedClassesResult = findMarkedClasses();
//        customizeContainerConfig(containerConfig);
//        initInnerContainer();
    }

    public void wire(Object bean) {
        initInnerContainer();
//        prototypeContainer.wire(bean, WiringMode.AUTOWIRE);
    }

//    public PetiteConfig customizeContainerConfig(PetiteConfig containerConfig) {
//        return containerConfig;
//    }
//
//    public PetiteContainer getParentContainer() {
//        return parentContainer;
//    }
//
//    public PetiteContainer getPrototypeContainer() {
//        return prototypeContainer;
//    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String id) {
//        initInnerContainer();
//        Object bean = prototypeContainer.getBean(id);
//        if (bean != null)
//            return (T) bean;
//        else
            return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<?> type) {
//        initInnerContainer();
//        Object bean = prototypeContainer.getBean(type);
//        if (bean != null)
//            return (T) bean;
//        else
            return null;
    }

    public void addSingletonBean(String name, Object bean) {
//        parentContainer.addBean(name, bean);
    }

    public void addBean(String name, Object bean) {
//        prototypeContainer.addBean(name, bean);
    }

//    public MarkedClassesResult findMarkedClasses() {
//        MarkedClassFinder markedClassFinder = new MarkedClassFinder();
//        markedClassFinder.scan();
//        return markedClassFinder.getMarkedClassesResult();
//    }

    private void initInnerContainer() {
        initSingletonContainer();
        initPrototypeContainer();
    }

    public void initSingletonContainer(){
        // init shared services' container
        if (parentContainerStarted.compareAndSet(false, true)) {
//            parentContainer = new PetiteContainer(containerConfig);
//            enableParentContainerAnnotationScan(parentContainer, markedClassesResult.get(MarkedClassType.SINGLETON));
        }
    }

    public void initPrototypeContainer(){
        // init prototype services' container
        if (containerInited.compareAndSet(false, true)) {
//            prototypeContainer = new HierarchyPetiteContainer(parentContainer);
//            enablePrototypeContainerAnnotationScan(prototypeContainer, markedClassesResult.get(MarkedClassType.SINGLETON));
//            addBean(ComponentNames.RPC_CONTAINER, this);
//            addBean(ComponentNames.APPLICATION_MODEL, ApplicationModelFactory.getCurrentApplication());
        }
    }
    public void wireApplicationAwares(){
        // wire services that need other services in container
//        if (markedClassesResult.get(MarkedClassType.RPC_CONTAINER_AWARE).length > 0) {
//            wireApplicationToAwares(markedClassesResult.get(MarkedClassType.RPC_CONTAINER_AWARE));
//        }
    }

    private void wireApplicationToAwares(String[] classes) {
        for (String className : classes) {
//            try {
//                prototypeContainer.wire(prototypeContainer.getBean(Class.forName(className)));
//            } catch (ClassNotFoundException e) {
//                 never happen? logger haven't init..
//                System.err.println("Could not find class for [" + className + "].So this component cannot be wired by rpcContainer");
//            }
        }
    }

//    private void enableParentContainerAnnotationScan(PetiteContainer parentContainer, String... includePackages) {
//        AutomagicPetiteConfigurator configurator = new AutomagicPetiteConfigurator();
//        configurator.setExcludeAllEntries(true);
//        configurator.setIncludedEntries(includePackages);
//        configure(configurator, parentContainer);
//    }

//    private void enablePrototypeContainerAnnotationScan(PetiteContainer prototypeContainer, String... excludePackages) {
//        AutomagicPetiteConfigurator configurator = new AutomagicPetiteConfigurator();
//        configurator.setExcludeAllEntries(true);
//        configurator.setIncludedEntries("com.taobao.hsf.*", "com.alibaba.dubbo.*", "com.taobao.hsf2dubbo.*");
//        configurator.setExcludedEntries(excludePackages);
//        configure(configurator, prototypeContainer);
//    }

//    private void configure(AutomagicPetiteConfigurator configurator, PetiteContainer container) {
//        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//        try {
//            Thread.currentThread().setContextClassLoader(RpcContainer.class.getClassLoader());
//            configurator.configure(container);
//        } finally {
//            Thread.currentThread().setContextClassLoader(contextClassLoader);
//        }
//    }

}