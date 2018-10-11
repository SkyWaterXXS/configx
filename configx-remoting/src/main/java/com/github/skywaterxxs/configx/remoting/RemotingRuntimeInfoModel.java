package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.RemotingRuntimeInfoModel</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/1 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RemotingRuntimeInfoModel {
    private String methodName;
    private Object[] inParams;
    private Object result;
    private long time;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getInParams() {
        return inParams;
    }

    public void setInParams(Object[] inParams) {
        this.inParams = inParams;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
//
//    @Override
//    public String toString() {
//        return methodName + "\t" + StringUtils.join(inParams, ",") + "\t" + result.toString() + "\t" + time + "\r\n";
//    }
}
