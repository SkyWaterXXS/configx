package com.github.skywaterxxs.common;

/**
 * @author xuxiaoshuo 2018/4/11
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.StringUtils;

public class JsonUtil {

    public static JsonMapper jsonMapper = new JsonMapper(true, false, JacksonDateFormat.PATTERN_YYYYMMDDHHMMSS);

    private JsonUtil() {
    }

    public static ObjectMapper instance() {
        return jsonMapper.getDelegate();
    }

    public static String of(Object o) {
        return jsonMapper.writeValueAsString(o);
    }

    @SuppressWarnings("unchecked")
    public static <T> T of(String json, Class<T> tClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return jsonMapper.readValue(json, tClass);
    }

    public static <T> T of(String json, TypeReference<T> reference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return jsonMapper.readerFor(reference).readValue(json);
    }

    public static <T> T of(String json, Class<T> clazz, Module module) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        jsonMapper.registerModule(module);
        return jsonMapper.readerFor(clazz).readValue(json);
    }

    public static <T> List<T> ofList(String json, Class<T> tClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        JavaType javaType = jsonMapper.getTypeFactory().constructParametrizedType(ArrayList.class, ArrayList.class,
                tClass);
        return jsonMapper.readValue(json, javaType);
    }

    public static <K, V> Map<K, V> ofMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        MapType mapType = jsonMapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
        return jsonMapper.readValue(json, mapType);
    }

    public static String toJson(Object obj) {
        if (null == obj) {
            return null;
        }
        return jsonMapper.writeValueAsString(obj);
    }
}

