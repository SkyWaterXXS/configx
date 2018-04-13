package com.github.skywaterxxs.common;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xuxiaoshuo 2018/4/11
 */
public class JsonWriter {

    private final ObjectWriter current;

    public JsonWriter(ObjectWriter current) {
        this.current = current;
    }

    public Version version() {
        return current.version();
    }

    private JsonWriter check(ObjectWriter target) {
        return target == current ? this : new JsonWriter(target);
    }

    public JsonWriter with(SerializationFeature feature) {
        return check(current.with(feature));
    }

    public JsonWriter with(SerializationFeature first, SerializationFeature... other) {
        return check(current.with(first, other));
    }

    public JsonWriter withFeatures(SerializationFeature... features) {
        return check(current.withFeatures(features));
    }

    public JsonWriter without(SerializationFeature feature) {
        return check(current.without(feature));
    }

    public JsonWriter without(SerializationFeature first, SerializationFeature... other) {
        return check(current.without(first, other));
    }

    public JsonWriter withoutFeatures(SerializationFeature... features) {
        return check(current.withoutFeatures(features));
    }

    public JsonWriter with(JsonGenerator.Feature feature) {
        return check(current.with(feature));
    }

    public JsonWriter withFeatures(JsonGenerator.Feature... features) {
        return check(current.withFeatures(features));
    }

    public JsonWriter without(JsonGenerator.Feature feature) {
        return check(current.without(feature));
    }

    public JsonWriter withoutFeatures(JsonGenerator.Feature... features) {
        return check(current.withoutFeatures(features));
    }

    public JsonWriter with(FormatFeature feature) {
        return check(current.with(feature));
    }

    public JsonWriter withFeatures(FormatFeature... features) {
        return check(current.withFeatures(features));
    }

    public JsonWriter without(FormatFeature feature) {
        return check(current.without(feature));
    }

    public JsonWriter withoutFeatures(FormatFeature... features) {
        return check(current.withoutFeatures(features));
    }

    public JsonWriter forType(JavaType rootType) {
        return check(current.forType(rootType));
    }

    public JsonWriter forType(Class<?> rootType) {
        return check(current.forType(rootType));
    }

    public JsonWriter forType(TypeReference<?> rootType) {
        return check(current.forType(rootType));
    }

    public JsonWriter with(DateFormat df) {
        return check(current.with(df));
    }

    public JsonWriter withDefaultPrettyPrinter() {
        return check(current.withDefaultPrettyPrinter());
    }

    public JsonWriter with(FilterProvider filterProvider) {
        return check(current.with(filterProvider));
    }

    public JsonWriter with(PrettyPrinter pp) {
        return check(current.with(pp));
    }

    public JsonWriter withRootName(String rootName) {
        return check(current.withRootName(rootName));
    }

    public JsonWriter withRootName(PropertyName rootName) {
        return check(current.withRootName(rootName));
    }

    public JsonWriter withoutRootName() {
        return check(current.withoutRootName());
    }

    public JsonWriter with(FormatSchema schema) {
        return check(current.with(schema));
    }

    public JsonWriter withView(Class<?> view) {
        return check(current.withView(view));
    }

    public JsonWriter with(Locale l) {
        return check(current.with(l));
    }

    public JsonWriter with(TimeZone tz) {
        return check(current.with(tz));
    }

    public JsonWriter with(Base64Variant b64variant) {
        return check(current.with(b64variant));
    }

    public JsonWriter with(CharacterEscapes escapes) {
        return check(current.with(escapes));
    }

    public JsonWriter with(JsonFactory f) {
        return check(current.with(f));
    }

    public JsonWriter with(ContextAttributes attrs) {
        return check(current.with(attrs));
    }

    public JsonWriter withAttributes(Map<?, ?> attrs) {
        return check(current.withAttributes(attrs));
    }

    public JsonWriter withAttribute(Object key, Object value) {
        return check(current.withAttribute(key, value));
    }

    public JsonWriter withoutAttribute(Object key) {
        return check(current.withoutAttribute(key));
    }

    public JsonWriter withRootValueSeparator(String sep) {
        return check(current.withRootValueSeparator(sep));
    }

    public JsonWriter withRootValueSeparator(SerializableString sep) {
        return check(current.withRootValueSeparator(sep));
    }

    public SequenceWriter writeValues(File out) throws JsonException {
        try {
            return current.writeValues(out);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValues(JsonGenerator gen) throws JsonException {
        try {
            return current.writeValues(gen);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValues(Writer out) throws JsonException {
        try {
            return current.writeValues(out);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValues(OutputStream out) throws JsonException {
        try {
            return current.writeValues(out);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValuesAsArray(File out) throws JsonException {
        try {
            return current.writeValuesAsArray(out);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValuesAsArray(JsonGenerator gen) throws JsonException {
        try {
            return current.writeValuesAsArray(gen);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValuesAsArray(Writer out) throws JsonException {
        try {
            return current.writeValuesAsArray(out);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public SequenceWriter writeValuesAsArray(OutputStream out) throws JsonException {
        try {
            return current.writeValuesAsArray(out);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public boolean isEnabled(SerializationFeature f) {
        return current.isEnabled(f);
    }

    public boolean isEnabled(MapperFeature f) {
        return current.isEnabled(f);
    }

    public boolean isEnabled(JsonParser.Feature f) {
        return current.isEnabled(f);
    }

    public SerializationConfig getConfig() {
        return current.getConfig();
    }

    public JsonFactory getFactory() {
        return current.getFactory();
    }

    public TypeFactory getTypeFactory() {
        return current.getTypeFactory();
    }

    public boolean hasPrefetchedSerializer() {
        return current.hasPrefetchedSerializer();
    }

    public ContextAttributes getAttributes() {
        return current.getAttributes();
    }

    public void writeValue(JsonGenerator gen, Object value) throws JsonException {
        try {
            current.writeValue(gen, value);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(File resultFile, Object value) throws JsonException {
        try {
            current.writeValue(resultFile, value);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(OutputStream out, Object value) throws JsonException {
        try {
            current.writeValue(out, value);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(Writer w, Object value) throws JsonException {
        try {
            current.writeValue(w, value);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public String writeValueAsString(Object value) throws JsonException {
        try {
            return current.writeValueAsString(value);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public byte[] writeValueAsBytes(Object value) throws JsonException {
        try {
            return current.writeValueAsBytes(value);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonException {
        try {
            current.acceptJsonFormatVisitor(type, visitor);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void acceptJsonFormatVisitor(Class<?> rawType, JsonFormatVisitorWrapper visitor) throws JsonException {
        try {
            current.acceptJsonFormatVisitor(rawType, visitor);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return current.canSerialize(type);
    }

    public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
        return current.canSerialize(type, cause);
    }

}

