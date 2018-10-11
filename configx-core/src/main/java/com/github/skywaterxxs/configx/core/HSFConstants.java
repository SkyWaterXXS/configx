package com.github.skywaterxxs.configx.core;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.HSFConstants</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class HSFConstants {


    public static final String PACKAGE_NAME = "com.taobao.hsf.*";

    /**
     * 标志此HSF的版本
     */
    public static final String RPC_VERSION = "v";

    /**
     * HTTP Transport port
     */
    public static final String HTTP_PORT = "_h";

    /**
     * HSF2.0优先使用的序列化方式，只会被2.0认出，因为2.0支持的序列化方式，1.0不能识别
     */
    public static final String PREFER_SERIALIZIER = "_p";

    /**
     * 客户端最大连接数Key
     */
    public static final String CLIENTMAXCONNECTION_KEY = "CLIENTMAXCONNECTION";

    /**
     * 客户端最小连接数Key
     */
    public static final String CLIENTMINCONNECTION_KEY = "CLIENTMINCONNECTION";

    /**
     * 客户端最大重试获取连接次数Key
     */
    public static final String CLIENTRETRYCONNECTIONTIMES_KEY = "CLIENTRETRYCONNECTIONTIMES";

    /**
     * 客户端重试获取连接的超时时间Key
     */
    public static final String CLIENTRETRYCONNECTIONTIMEOUT_KEY = "CLIENTRETRYCONNECTIONTIMEOUT";

    /**
     * 消费者的性能日志默认阀值
     */
    public static final int CONSUMER_PROFILER = 3000;

    /**
     * 提供者的性能日志默认阀值
     */
    public static final int PROVIDER_PROFILER = 2500;

    /**
     * 序列化类型
     */
    public static final String SERIALZETYPE_KEY = "SERIALZETYPE";

    /**
     * Hessian序列化
     */
    public static final String HESSIAN_SERIALIZE = "hessian";

    /**
     * Hessian2序列化
     */
    public static final String HESSIAN2_SERIALIZE = "hessian2";

    /**
     * Java序列化
     */
    public static final String JAVA_SERIALIZE = "java";

    /**
     * kryo序列化
     */
    public static final String KRYO_SERIALIZE = "kryo";

    /**
     * FastJson序列化
     */
    public static final String FASTJSON_SERIALIZE = "json";

    /**
     * Customized序列化
     */
    public static final String CUSTIMIZED_SERIALIZE = "custom";

    public static final String[] SUPPORTED_SERIALIZER = { "kryo", "java", "hessian", "hessian2", "json", "custom" };

    /**
     * stable_switch Key
     */
    public static final String STABLE_SWITCH = "stable_switch";

    /**
     * 未知应用名称
     */
    public static final String UNKNOWN_APP_NAME = "UNKNOWN";

    /**
     * 设置在HSFRequest中的调用应用名称的Key
     */
    public static final String CONSUMER_APP_NAME = "Consumer-AppName";

    /**
     * HSF日志目录标识Key
     */
    public static final String HSF_LOG_PATH = "HSF.LOG.PATH";

    public static final String COMPONENT_RUNTIME_INFO = "_runtime_info";
    public static final String SERVICES_CONSUMED = "services_consumed";
    public static final String SERVICES_PROVIDED = "services_provided";

    public static final String COMPONENT_NIOPROVIDER_SERVER = "_nio_provider_server";
    public static final String THREADPOOL_STORE_KEY = "_threadpool";
    public static final String METADATAS_STORE_KEY = "_metadatas";
    public static final String WORKS_STORE_KEY = "_works";
    public static final String WORKERMETHODS_STORE_KEY = "_workermethods";
    public static final String WORKERFIELDS_STORE_KEY = "_workerfields";
    public static final String LOG_METHODS_STORE_KEY = "_log_methods";

    /**
     * HSF ThreadLocal变量Key
     */
    // public static final String TL_KEY_PROTOCOL = "_serialize_protocol";
    public static final String TL_KEY_SERVICE_NAME = "_service_unique_name";
    public static final String TL_KEY_METHOD_NAME = "_method_name";
    public static final String TL_KEY_REMOTE_IP = "_remote_ip";
    // public static final String TL_KEY_REMOTE_IP_OF_CONSUMER = "_remote_ip_c";
    public static final String TL_KEY_METHOD_PARAMETERTYPES = "_method_parameter_types";
    public static final String TL_KEY_HSFREQUEST = "_hsf_request";
    public static final String TL_KEY_APPNAME_OF_CONSUMER = "_hsf_appname_c";
    public static final String TL_KEY_TARGET_SERVER_IP = "_hsf_target_server_ip";
    public static final String TL_KEY_REQUEST_TIMEOUT = "_hsf_request_timeout";

    /*
     * 用于特殊支持TOP和泛华调用的单元化
     */
    public static final String KEY_UNIT_DEPLOY_USER_ID = "_hsf_unit_deploy_user_id";

    public static final String UNIT_KEY = "unit_key";

    public static final String TARGET_GROUP = "target_group";

    /**
     * Rule header list
     */
    public static final String HEADER_FLOW_CONTROL_RULE = "flowControl@";
    public static final String HEADER_ROUTING_RULE = "Groovy_v200907@";

    public static final String PROXY_STYLE_JAVASSIST = "javassist";

    /**
     * 泛化
     */
    public static final String GENERIC_SERIALIZATION_NATIVE_JAVA = "nativejava";
    public static final String GENERIC_SERIALIZATION_DEFAULT = "true";
    public static final String $INVOKE = "$invoke";

    /**
     * ECHO
     */
    public static final String $ECHO = "$echo";

    public static final String NULL_VERSION = "0.0.0";

    /**
     * 调用方式标识字符串
     */
    public static final String INVOKE_TYPE_CALL_BACK = "CALLBACK";
    public static final String INVOKE_TYPE_FUTURE = "FUTURE";
    public static final String INVOKE_TYPE_RELIABLE = "RELIABLE";

    public static final String TARGET_SERVER_IP = "target_server_ip";
    /**
     * 安全验证的key，用于某些敏感应用的验证
     */
    public static final String SECURE_KEY = "Secure-Key";

    public static final String ACCESS_KEY = "Access-Key";

    public static final String SPAS_VERSION = "Spas-Version";

    public static final String SPAS_SIGNATURE = "Spas-Signature";

    public static final String TIME_STAMP = "Time-Stamp";

    public static final String CONSUMER_TIMEOUT = "Consumer-Timeout";

    public static final String TARGET_UNIT = "target-unit";

    String ROUTE_SERVICE = "ROUTE_SERVICE";

    String ROUTE_SERVICE_RANDOM = "random";

    String ROUTE_SERVICE_CONSISTENT = "consistent";

    String CONSISTENT_KEY = "CONSISTENT_KEY";
}
