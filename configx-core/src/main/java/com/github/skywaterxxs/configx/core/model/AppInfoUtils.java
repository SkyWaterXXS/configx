package com.github.skywaterxxs.configx.core.model;

import com.github.skywaterxxs.configx.core.HSFConstants;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.AppInfoUtils</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class AppInfoUtils {

    // 环境变量或启动参数标识
    public static final String PARAM_MARKING_PROJECT = "project.name";
    private static final String PARAM_MARKING_JBOSS = "jboss.server.home.dir";
    private static final String PARAM_MARKING_JETTY = "jetty.home";
    private static final String PARAM_MARKING_TOMCAT = "catalina.base";

    private static final String LINUX_ADMIN_HOME = "/home/admin/";
    private static final String SERVER_JBOSS = "jboss";
    private static final String SERVER_JETTY = "jetty";
    private static final String SERVER_TOMCAT = "tomcat";
    private static final String SERVER_UNKNOWN = "unknown server";

    private static String appName = null;
    private static String serverType = null;

    // xml方式配置的hsf服务是否启动成功 (spring初始化的所有行为都在同一线程中)
    public static final AtomicBoolean appRunning = new AtomicBoolean(false);
    public static int hsfSpringBeanCountDown = 0;
    public static int dubboSpringBeanCountDown = 0;

    static{
        initAppName();
    }

    /**
     * 从Java进程的启动参数中尝试获得应用名称<br />
     * <ol>
     * <li>使用<tt>project.name</tt>获得应用名称，获取成功则返回
     * <li>使用服务器启动参数获得应用名称，例如<tt>jboss.server.home.dir</tt>
     * <li>返回<tt>null</tt>
     * </ol>
     *
     * @return <tt>appName</tt> or <tt>null</tt>
     */
    public static String getAppName() {
        return appName;
    }

    public static void initAppName() {
        if (appName != null) {
            return;
        }
        // get from project.name parameter
        appName = System.getProperty(PARAM_MARKING_PROJECT);
        if (appName != null) {
            return;
        }

        // get from server home parameter
        String serverHome = null;
        if (SERVER_JBOSS.equals(getServerType())) {
            serverHome = System.getProperty(PARAM_MARKING_JBOSS);
        } else if (SERVER_JETTY.equals(getServerType())) {
            serverHome = System.getProperty(PARAM_MARKING_JETTY);
        } else if (SERVER_TOMCAT.equals(getServerType())) {
            serverHome = System.getProperty(PARAM_MARKING_TOMCAT);
        }

        if (serverHome != null && serverHome.startsWith(LINUX_ADMIN_HOME)) {
            appName = StringUtils.substringBetween(serverHome, LINUX_ADMIN_HOME, File.separator);
        }
        if (appName != null) {
            return;
        }

//        try {
//            Class<?> clazz = ClassUtils.forName(metadata.getInterfaceName());
//            String path = clazz.getResource("/").getPath();
//            File f = new File(path);
//            if (f.exists()) {
//                String fileName = f.getName();
//                int index = fileName.lastIndexOf(".");
//                if (index > 0) {
//                    fileName = fileName.substring(0, index);
//                }
//                appName = fileName;
//            } else {
//                LOGGER.error("", "[AppInfoUtils] Get application name failed, file not found: " + f);
//            }
//        } catch (Throwable t) {
//            LOGGER.error("", "[AppInfoUtils] Get application name failed: " + t.getMessage());
//        }

        if (appName == null) {
            appName = "unknown";
        }
    }

    /**
     * @return HSF日志路径
     */
    public static String getHSFLogPath() {
        return System.getProperty(HSFConstants.HSF_LOG_PATH);
    }

    /**
     * @return 应用服务器类型<tt>jboss</tt>, <tt>jetty</tt> or <tt>tomcat</tt>
     */
    public static String getServerType() {
        if (serverType != null) {
            return serverType;
        }

        if (System.getProperty(PARAM_MARKING_JBOSS) != null) {
            serverType = SERVER_JBOSS;
        } else if (System.getProperty(PARAM_MARKING_JETTY) != null) {
            serverType = SERVER_JETTY;
        } else if (System.getProperty(PARAM_MARKING_TOMCAT) != null) {
            serverType = SERVER_TOMCAT;
        } else {
            serverType = SERVER_UNKNOWN;
        }
        return serverType;
    }
}
