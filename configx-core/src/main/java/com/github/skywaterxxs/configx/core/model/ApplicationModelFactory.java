package com.github.skywaterxxs.configx.core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ApplicationModelFactory</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ApplicationModelFactory {

//    private static final Logger LOGGER = LoggerInit.ORIGIN_LOGGER;

    private final static ConcurrentMap<ClassLoader, ApplicationModel> loader2Application = new ConcurrentHashMap<ClassLoader, ApplicationModel>();

    private final static ConcurrentMap<String, ApplicationModel> providerService2App = new ConcurrentHashMap<String, ApplicationModel>();

    private final static ConcurrentMap<String, ProviderServiceModel> providerService2Model = new ConcurrentHashMap<String, ProviderServiceModel>();

    private static ThreadLocal<ApplicationModel> currentApplication = new ThreadLocal<ApplicationModel>();

    private static volatile boolean pandora_init = false;

    private final static AtomicReference<ApplicationModel> SINGLETON_APP = new AtomicReference<ApplicationModel>();

    private static volatile ApplicationModel mainApplicationModel = null;


    public static void initApplication(String name, ClassLoader appContextClassLoader, boolean isMain) {
        pandora_init = true;
        ApplicationModel applicationModel = new ApplicationModel(name, appContextClassLoader);
        applicationModel.setMain(isMain);
        if (isMain) {
            mainApplicationModel = applicationModel;
        }
        loader2Application.putIfAbsent(appContextClassLoader, applicationModel);
    }


    public static ApplicationModel setCurrentApplication() {
        if (pandora_init) {
            //assume that tccl is contextclassloader
            ApplicationModel applicationModel = loader2Application.get(Thread.currentThread().getContextClassLoader());
            if (applicationModel == null) {
                throw new IllegalStateException("no application found for TCCL[" + Thread.currentThread().getContextClassLoader() + "]");
            }
            currentApplication.set(applicationModel);
            return applicationModel;
        } else {
            //without pandora
            ApplicationModel applicationModel = SINGLETON_APP.get();
            if (applicationModel == null) {
                String name = AppInfoUtils.getAppName();
                applicationModel = new ApplicationModel(name, Thread.currentThread().getContextClassLoader());
                applicationModel.setMain(true);
                if (!SINGLETON_APP.compareAndSet(null, applicationModel)) {
                    applicationModel = SINGLETON_APP.get();
                }
            }
            mainApplicationModel = applicationModel;
            currentApplication.set(applicationModel);
            loader2Application.putIfAbsent(applicationModel.getAppContextClassLoader(), applicationModel);
            return applicationModel;
        }
    }


//    public static ApplicationModel setCurrentApplication(ClassLoader classLoader){
//        ApplicationModel applicationModel = loader2Application.get(classLoader);
//        if(applicationModel == null){
//            applicationModel = new ApplicationModel(classLoader);
//            loader2Application.putIfAbsent(classLoader,applicationModel);
//            applicationModel = loader2Application.get(classLoader);
//        }
//        currentApplication.set(applicationModel);
//        return applicationModel;
//    }
//
//    public static void createApplication(ClassLoader loader,String name){
//        ApplicationModel applicationModel =setCurrentApplication(loader);
//        applicationModel.setName(name);
//    }

    public static void setCurrentApplication(ApplicationModel application) {
        currentApplication.set(application);
    }

    public static void setCurrentApplicationToMainApp() {
        currentApplication.set(getMainApplicationModel());
    }

    public static ApplicationModel getCurrentApplication() {
        //failover to main application
        if (currentApplication.get() == null) {
//            String stack = ExceptionUtils.getFullStackTrace(new IllegalStateException("Application not found"));
//            LOGGER.info(stack);
            //currentApplication.set(new ApplicationModel(AppInfoUtils.getAppName(),ApplicationModelFactory.class.getClassLoader()));
            currentApplication.set(getMainApplicationModel());
        }
        return currentApplication.get();
    }

    public static ApplicationModel unsafeGetCurrentApplication() {
        return currentApplication.get();
    }

    public static ClassLoader getCurrentApplicationLoader() {
        return getCurrentApplication().getAppContextClassLoader();
    }

    public static ApplicationModel getApplication(String serviceUniqueName) {
        return providerService2App.get(serviceUniqueName);
    }

    public static void setApplication(String serviceUniqueName) {
        setCurrentApplication(providerService2App.get(serviceUniqueName));
    }

    public static void put(String serviceUniqueName, ApplicationModel applicationModel) {
        ApplicationModel old = providerService2App.putIfAbsent(serviceUniqueName, applicationModel);
        if (old != null && old != applicationModel) {
            String errorMessage = "app[" + applicationModel.getName() + "] and app[" + old.getName() + "] publish same service[" + serviceUniqueName + "]";
            throw new IllegalStateException(errorMessage);
        }
    }

    public static ProviderServiceModel getProviderServiceModel(String serviceUniqueName) {
        return providerService2Model.get(serviceUniqueName);
    }

    public static void put(String serviceUniqueName, ProviderServiceModel providerServiceModel) {
        providerService2Model.putIfAbsent(serviceUniqueName, providerServiceModel);
    }


    public static boolean isCodeploy() {
        return loader2Application.size() > 1;
    }

    public static Set<ApplicationModel> getApplication() {
        Set<ApplicationModel> apps = new HashSet<ApplicationModel>();
        if (loader2Application.isEmpty()) {
            apps.add(SINGLETON_APP.get());
        } else {
            apps.addAll(loader2Application.values());
        }
        return apps;
    }

    public static ApplicationModel getMainApplicationModel() {
        if (mainApplicationModel == null) {
            mainApplicationModel = new ApplicationModel(AppInfoUtils.getAppName(), ApplicationModelFactory.class.getClassLoader());
        }
        return mainApplicationModel;
    }

    public static ApplicationModel getApplicationByAppName(String appName) {
        for (ApplicationModel applicationModel : loader2Application.values()) {
            if (applicationModel.getName().equals(appName)) {
                return applicationModel;
            }
        }
        return null;
    }

    public static ApplicationModel getApplicationByLoader(ClassLoader classLoader) {
        return loader2Application.get(classLoader);
    }

}