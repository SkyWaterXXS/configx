package com.github.skywaterxxs.common;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;

/**
 * @author xuxiaoshuo 2018/4/11
 */
public class DateTimeSerializer extends com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer {

    private boolean custom = false;

    public DateTimeSerializer(JacksonJodaDateFormat format, boolean custom) {
        super(format);
        this.custom = custom;
    }

    @Override
    public com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer withFormat(JacksonJodaDateFormat formatter) {
        return new DateTimeSerializer(formatter, true);
    }

    @Override
    protected boolean _useTimestamp(SerializerProvider provider) {
        if (custom) {
            return false;
        }
        return super._useTimestamp(provider);
    }
}
