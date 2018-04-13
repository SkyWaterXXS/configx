package com.github.skywaterxxs.common;

/**
 * @author xuxiaoshuo 2018/4/11
 */

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * {@link ObjectReader} 包装，修改主要方法返回运行时异常{@link JsonException}。
 *
 * @author Daniel Li
 * @since 04 May 2017
 */
public class JsonReader {

    private final ObjectReader current;

    public JsonReader(ObjectReader current) {
        this.current = current;
    }

    public Version version() {
        return current.version();
    }

    private JsonReader check(ObjectReader target) {
        return current == target ? this : new JsonReader(target);
    }

    public JsonReader with(DeserializationFeature feature) {
        return check(current.with(feature));
    }


    public JsonReader with(DeserializationFeature first, DeserializationFeature... other) {
        return check(current.with(first, other));
    }

    public JsonReader withFeatures(DeserializationFeature... features) {
        return check(current.withFeatures(features));
    }

    public JsonReader without(DeserializationFeature feature) {
        return check(current.without(feature));
    }

    public JsonReader without(DeserializationFeature first, DeserializationFeature... other) {
        return check(current.without(first, other));
    }

    public JsonReader withoutFeatures(DeserializationFeature... features) {
        return check(current.withoutFeatures(features));
    }

    public JsonReader with(JsonParser.Feature feature) {
        return check(current.with(feature));
    }

    public JsonReader withFeatures(JsonParser.Feature... features) {
        return check(current.withFeatures(features));
    }

    public JsonReader without(JsonParser.Feature feature) {
        return check(current.without(feature));
    }

    public JsonReader withoutFeatures(JsonParser.Feature... features) {
        return check(current.withoutFeatures(features));
    }

    public JsonReader with(FormatFeature feature) {
        return check(current.with(feature));
    }

    public JsonReader withFeatures(FormatFeature... features) {
        return check(current.withFeatures(features));
    }

    public JsonReader without(FormatFeature feature) {
        return check(current.without(feature));
    }

    public JsonReader withoutFeatures(FormatFeature... features) {
        return check(current.withoutFeatures(features));
    }

    public JsonReader with(DeserializationConfig config) {
        return check(current.with(config));
    }

    public JsonReader with(InjectableValues injectableValues) {
        return check(current.with(injectableValues));
    }

    public JsonReader with(JsonNodeFactory f) {
        return check(current.with(f));
    }

    public JsonReader with(JsonFactory f) {
        return check(current.with(f));
    }

    public JsonReader withRootName(String rootName) {
        return check(current.withRootName(rootName));
    }

    public JsonReader withRootName(PropertyName rootName) {
        return check(current.withRootName(rootName));
    }

    public JsonReader withoutRootName() {
        return check(current.withoutRootName());
    }

    public JsonReader with(FormatSchema schema) {
        return check(current.with(schema));
    }

    public JsonReader forType(JavaType valueType) {
        return check(current.forType(valueType));
    }

    public JsonReader forType(Class<?> valueType) {
        return check(current.forType(valueType));
    }

    public JsonReader forType(TypeReference<?> valueTypeRef) {
        return check(current.forType(valueTypeRef));
    }

    public JsonReader withValueToUpdate(Object value) {
        return check(current.withValueToUpdate(value));
    }

    public JsonReader withView(Class<?> activeView) {
        return check(current.withView(activeView));
    }

    public JsonReader with(Locale l) {
        return check(current.with(l));
    }

    public JsonReader with(TimeZone tz) {
        return check(current.with(tz));
    }

    public JsonReader withHandler(DeserializationProblemHandler h) {
        return check(current.withHandler(h));
    }

    public JsonReader with(Base64Variant defaultBase64) {
        return check(current.with(defaultBase64));
    }

    public JsonReader withFormatDetection(ObjectReader... readers) {
        return check(current.withFormatDetection(readers));
    }

    public JsonReader withFormatDetection(DataFormatReaders readers) {
        return check(current.withFormatDetection(readers));
    }

    public JsonReader with(ContextAttributes attrs) {
        return check(current.with(attrs));
    }

    public JsonReader withAttributes(Map<?, ?> attrs) {
        return check(current.withAttributes(attrs));
    }

    public JsonReader withAttribute(Object key, Object value) {
        return check(current.withAttribute(key, value));
    }

    public JsonReader withoutAttribute(Object key) {
        return check(current.withoutAttribute(key));
    }

    public boolean isEnabled(DeserializationFeature f) {
        return current.isEnabled(f);
    }

    public boolean isEnabled(MapperFeature f) {
        return current.isEnabled(f);
    }

    public boolean isEnabled(JsonParser.Feature f) {
        return current.isEnabled(f);
    }

    public DeserializationConfig getConfig() {
        return current.getConfig();
    }

    public JsonFactory getFactory() {
        return current.getFactory();
    }

    public TypeFactory getTypeFactory() {
        return current.getTypeFactory();
    }

    public ContextAttributes getAttributes() {
        return current.getAttributes();
    }

    public InjectableValues getInjectableValues() {
        return current.getInjectableValues();
    }

    public <T> T readValue(JsonParser p) throws JsonException {
        try {
            return current.readValue(p);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonParser p, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(p, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(p, valueTypeRef);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonParser p, ResolvedType valueType) throws JsonException {
        try {
            return current.readValue(p, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonParser p, JavaType valueType) throws JsonException {
        try {
            return current.readValue(p, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> Iterator<T> readValues(JsonParser p, Class<T> valueType) throws JsonException {
        try {
            return current.readValues(p, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> Iterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValues(p, valueTypeRef);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> Iterator<T> readValues(JsonParser p, ResolvedType valueType) throws JsonException {
        try {
            return current.readValues(p, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> Iterator<T> readValues(JsonParser p, JavaType valueType) throws JsonException {
        try {
            return current.readValues(p, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode createArrayNode() {
        return current.createArrayNode();
    }

    public JsonNode createObjectNode() {
        return current.createObjectNode();
    }

    public JsonParser treeAsTokens(TreeNode n) {
        return current.treeAsTokens(n);
    }

    public <T extends TreeNode> T readTree(JsonParser p) throws JsonException {
        try {
            return current.readTree(p);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(InputStream src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(Reader src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(String src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }


    public <T> T readValue(byte[] src, int offset, int length) throws JsonException {
        try {
            return current.readValue(src, offset, length);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(File src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(URL src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonNode src) throws JsonException {
        try {
            return current.readValue(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(InputStream in) throws JsonException {
        try {
            return current.readTree(in);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(Reader r) throws JsonException {
        try {
            return current.readTree(r);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(String json) throws JsonException {
        try {
            return current.readTree(json);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p) throws JsonException {
        try {
            return current.readTree(p);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(InputStream src) throws JsonException {
        try {
            return current.readValues(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(Reader src) throws JsonException {
        try {
            return current.readValues(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(String json) throws JsonException {
        try {
            return current.readValues(json);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws JsonException {
        try {
            return current.readValues(src, offset, length);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public final <T> MappingIterator<T> readValues(byte[] src) throws JsonException {
        try {
            return current.readValues(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(File src) throws JsonException {
        try {
            return current.readValues(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(URL src) throws JsonException {
        try {
            return current.readValues(src);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonException {
        try {
            return current.treeToValue(n, valueType);
        }
        catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonReader at(String value) {
        return new JsonReader(current.at(value));
    }

    public JsonReader at(JsonPointer pointer) {
        return new JsonReader(current.at(pointer));
    }
}
