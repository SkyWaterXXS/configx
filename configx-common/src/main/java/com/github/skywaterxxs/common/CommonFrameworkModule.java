package com.github.skywaterxxs.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author xuxiaoshuo 2018/4/11
 */
public class CommonFrameworkModule extends SimpleModule {

    public CommonFrameworkModule() {
        super("CommonFrameworkModule", Version.unknownVersion());

        addSerializer(Money.class, MoneySerializer.INSTANCE);
        addDeserializer(Money.class, MoneyDeserializer.INSTANCE);

        // 兼容 jackson 2.5 以下的版本, 对 Map.Entry 序列化做特殊处理
        addSerializer(Map.Entry.class, new JsonSerializer<Map.Entry>() {
            @Override
            public void serialize(Map.Entry entry, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeObject(new KeyValue(entry.getKey(), entry.getValue()));
            }
        });
    }
}
