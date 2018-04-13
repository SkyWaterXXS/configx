package com.github.skywaterxxs.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author xuxiaoshuo 2018/4/11
 */
public class MoneySerializer extends JsonSerializer<Money> {

    public static MoneySerializer INSTANCE = new MoneySerializer();

    private MoneySerializer() {
    }


    @Override
    public void serialize(Money money, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeRawValue(money != null ? money.toScaleMoneyString() : "null");
    }
}