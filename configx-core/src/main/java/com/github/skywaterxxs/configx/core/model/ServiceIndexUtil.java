package com.github.skywaterxxs.configx.core.model;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.ServiceIndexUtil</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ServiceIndexUtil {

    public static final String getMethodNameToLog(final String methodName, final String[] methodArgSigs) {
        if (methodArgSigs == null || methodArgSigs.length == 0) {
            return methodName;
        }

        StringBuilder logMethodBuilder = new StringBuilder(methodName);
        logMethodBuilder.append('~');
        for (String argSig : methodArgSigs) {
            int index = argSig.lastIndexOf('.') + 1;
            logMethodBuilder.append(argSig.charAt(index));
        }

        return logMethodBuilder.toString();
    }
}
