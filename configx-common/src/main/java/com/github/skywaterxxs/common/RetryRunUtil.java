package com.github.skywaterxxs.common;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>ClassName:com.github.skywaterxxs.demo.common.RetryRunUtil</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/6 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class RetryRunUtil {


    /**
     * 无参数,无返回值
     */
    public static void runRetry(Runnable command, Integer maxRetryTime) {

        int runCount = 0;
        boolean needRun = true;
        while (needRun && runCount < maxRetryTime) {
            try {
                runCount++;
                command.run();
                needRun = false;
            } catch (Throwable e) {
                if (runCount >= maxRetryTime) {
                    throw e;
                }
            }
        }
    }

    /**
     * 无参数,有返回值
     */
    public static <T> T runRetryWithResult(Supplier<T> command, Integer maxRetryTime) {

        int runCount = 0;
        boolean needRun = true;
        T result = null;
        while (needRun && runCount < maxRetryTime) {
            try {
                runCount++;
                result = command.get();
                needRun = false;
            } catch (Throwable e) {
                if (runCount >= maxRetryTime) {
                    throw e;
                }
            }
        }
        return result;
    }
//
//    /**
//     * 有参数,有返回值
//     */
//    public static <T, R> R runRetry(T param, Function<T, R> command, Integer maxRetryTime) {
//
//        int runCount = 0;
//        boolean needRun = true;
//        R result = null;
//        while (needRun && runCount < maxRetryTime) {
//            try {
//                runCount++;
//                result = command.apply(param);
//                needRun = false;
//            } catch (Throwable e) {
//                if (runCount >= maxRetryTime) {
//                    throw e;
//                }
//            }
//        }
//        return result;
//    }

//    /**
//     * 有参数,无返回值
//     */
//    public static <T> void runRetry(T param, Consumer<T> command, Integer maxRetryTime) {
//
//        int runCount = 0;
//        boolean needRun = true;
//        while (needRun && runCount < maxRetryTime) {
//            try {
//                runCount++;
//                command.accept(param);
//                needRun = false;
//            } catch (Throwable e) {
//                if (runCount >= maxRetryTime) {
//                    throw e;
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 有参数,返回值 Boolean
//     */
//    public static <T> Boolean runRetry(T param, Predicate<T> command, Integer maxRetryTime) {
//
//        int runCount = 0;
//        boolean needRun = true;
//        Boolean result = null;
//        while (needRun && runCount < maxRetryTime) {
//            try {
//                runCount++;
//                result = command.test(param);
//                needRun = false;
//            } catch (Throwable e) {
//                if (runCount >= maxRetryTime) {
//                    throw e;
//                }
//            }
//        }
//        return result;
//    }


}
