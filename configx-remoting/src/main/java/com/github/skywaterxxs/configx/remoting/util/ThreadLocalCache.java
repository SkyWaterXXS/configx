package com.github.skywaterxxs.configx.remoting.util;

import com.github.skywaterxxs.configx.remoting.RemotingConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.remoting.util.ThreadLocalCache</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class ThreadLocalCache {

    private final static int MAP_MAX_SIZE = 10192;

    private final static ThreadLocal<Map<String, byte[]>> STRING_TO_BYTES = ThreadLocal.withInitial(() -> new HashMap<>(2048));
    private final static ThreadLocal<Map<BytesWrapper, String>> BYTES_TO_STRING = ThreadLocal.withInitial(() -> new HashMap<>(2048));

    public static byte[] getBytes(String string) {
        Map<String, byte[]> map = STRING_TO_BYTES.get();
        byte[] result = map.get(string);
        if (result != null) {
            return result;
        }
        result = string.getBytes(RemotingConstants.DEFAULT_CHARSET);
        // 避免 memory leak 内存泄露
        if (map.size() < MAP_MAX_SIZE) {
            map.put(string, result);
        }
        return result;
    }

    public static String getString(byte[] bytes) {
        Map<BytesWrapper, String> map = BYTES_TO_STRING.get();
        String string = map.get(new BytesWrapper(bytes));
        if (string != null) {
            return string;
        }

        string = new String(bytes, RemotingConstants.DEFAULT_CHARSET);
        // 避免 memory leak 内存泄露
        if (map.size() < MAP_MAX_SIZE) {
            map.put(new BytesWrapper(bytes), string);
        }
        return string;
    }

    private static final class BytesWrapper {
        private final byte[] bytes;

        public BytesWrapper(final byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            return Arrays.equals(bytes, ((BytesWrapper) obj).bytes);
        }
    }
}
