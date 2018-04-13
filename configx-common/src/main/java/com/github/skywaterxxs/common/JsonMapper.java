package com.github.skywaterxxs.common;

/**
 * @author xuxiaoshuo 2018/4/11
 */

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

/**
 * {@link ObjectMapper} 包装，<a href="https://github.com/FasterXML/jackson-databind/wiki/JacksonFeatures">Jackson Features 参考</a>，内部改造如下
 * <ul>
 * <li>修改方法返回运行时异常{@link JsonException}</li>
 * <li>{@link com.fasterxml.jackson.databind.ObjectReader} 转 {@link JsonReader}</li>
 * <li>{@link com.fasterxml.jackson.databind.ObjectReader} 转 {@link JsonWriter}</li>
 * <li>默认构造方法，进行了一些配置的默认设置，兼容早期的JsonMapper实现</li>
 * </ul>
 * 默认构造方法与{@Olink ObjectMapper}主要区别，推荐使用默认构造方法。
 * <ul>
 * <li>关闭WRITE_NULL_MAP_VALUES特性，当Map中存在value等于null时不进行处理</li>
 * <li>关闭FAIL_ON_EMPTY_BEANS特征，当遇到一个对象无法进行序列化（没有对应类型的序列化器，以及没有任何注解用于识别如何序列化），则输出null值代替抛异常</li>
 * <li>关闭FAIL_ON_UNKNOWN_PROPERTIES特征，当解序列化时，如果遇到未知字段(JSON中的key在Object中不存在)，忽略代替抛异常</li>
 * <li>开启ALLOW_COMMENTS特征，解析JSON过程中，可以处理JSON中 // 和 /* 注释</li>
 * <li>开启ALLOW_YAML_COMMENTS特征，解析JSON过程中，支持按 # 开头的YARM注释风格</li>
 * <li>开启ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER特征，允许使用反斜杠机制进行转义</li>
 * <li>开启ALLOW_NON_NUMERIC_NUMBERS特征，允许识别NaN</li>
 * <li>开启ALLOW_NUMERIC_LEADING_ZEROS特征，允许JSON中整数以多个0开始</li>
 * <li>开启ALLOW_UNQUOTED_CONTROL_CHARS特征，允许字符串值包含非引号外的其他控制字符</li>
 * <li>开启ALLOW_UNQUOTED_FIELD_NAMES特征，允许使用非双引号属性名</li>
 * <li>开启ALLOW_SINGLE_QUOTES特征，允许单引号形式的属性名和字符串值</li>
 * </ul>
 *
 * @author Daniel Li
 * @since 04 May 2017
 */
public class JsonMapper {

    private final ObjectMapper current;

    public JsonMapper() {
        current = new ObjectMapper();
        configure(current, false);
    }

    public JsonMapper(boolean bizDateEnhance) {
        current = new ObjectMapper();
        configure(current, bizDateEnhance);
    }

    public JsonMapper(boolean timestampPriority, String formatPattern) {
        current = new ObjectMapper();
        configure(current, false, timestampPriority, formatPattern);
    }

    public JsonMapper(boolean bizDateEnhance, boolean timestampPriority, String formatPattern) {
        current = new ObjectMapper();
        configure(current, bizDateEnhance, timestampPriority, formatPattern);
    }

    public JsonMapper(ObjectMapper current) {
        this.current = current;
    }

    public static void configure(ObjectMapper current, boolean bizDateEnhance) {
        configure(current, bizDateEnhance, true, JacksonDateFormat.PATTERN_YYYYMMDDHHMMSSSSS);
    }

    public static void configure(ObjectMapper current, boolean bizDateEnhance, boolean timestampPriority, String formatPattern) {
        current.setTimeZone(TimeZone.getDefault());
        current.setDateFormat(new JacksonDateFormat(formatPattern));
        if (timestampPriority) {
            current.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        }

        current.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)                // 默认为true
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)                // 默认为true
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)        // 默认为true
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)       // 默认为false
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)                        // 默认为false
                .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)                    // 默认为false
                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)    // 默认为false
                .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)                // 默认为false
                .configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true)            // 默认为false
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)            // 默认为false
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)            // 默认为false
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)                    // 默认为false
                .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)                         // 默认为false
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        current.registerModules(new AfterburnerModule());
        current.registerModules(new GuavaModule());
        current.registerModules(new JodaModule(formatPattern));
        ClassLoader loader = JsonMapper.class.getClassLoader();

        if (ClassUtils.isPresent("java.util.Optional", loader)) {
            try {
                current.registerModule((Module) BeanUtils.instantiateClass(ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", loader)));
            } catch (ClassNotFoundException ex) {
            }
        }

        // Java 8 java.time package present?
        if (ClassUtils.isPresent("java.time.LocalDate", loader)) {
            try {
                current.registerModules((Module) BeanUtils.instantiate(ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", loader)));
            } catch (ClassNotFoundException e) {
            }
        }

        if (ClassUtils.isPresent("kotlin.Unit", loader)) {
            try {
                current.registerModules((Module) BeanUtils.instantiate(ClassUtils.forName("com.fasterxml.jackson.module.kotlin.KotlinModule", loader)));
            } catch (ClassNotFoundException e) {
            }
        }

        if (bizDateEnhance) {
            if (ClassUtils.isPresent("com.wormpex.api.json.BizDateStdModule", loader)) {
                try {
                    current.registerModules((Module) BeanUtils.instantiate(ClassUtils.forName("com.wormpex.api.json.BizDateStdModule", loader)));
                } catch (ClassNotFoundException e) {
                }
            }
        }
        CommonFrameworkModule commonFrameworkModule = new CommonFrameworkModule();
        current.registerModule(commonFrameworkModule);
    }

    public static List<Module> findModules() {
        return findModules(null);
    }

    public static List<Module> findModules(ClassLoader classLoader) {
        ArrayList<Module> modules = new ArrayList<>();
        ServiceLoader<Module> loader = (classLoader == null) ?
                ServiceLoader.load(Module.class) : ServiceLoader.load(Module.class, classLoader);
        for (Module module : loader) {
            modules.add(module);
        }
        return modules;
    }

    public ObjectMapper getDelegate() {
        return current;
    }

    public JsonMapper copy() {
        return new JsonMapper(current.copy());
    }

    public Version version() {
        return current.version();
    }

    public JsonMapper registerModule(Module module) {
        current.registerModule(module);
        return this;
    }

    public JsonMapper registerModules(Module... modules) {
        current.registerModules(modules);
        return this;
    }

    public JsonMapper registerModules(Iterable<Module> modules) {
        current.registerModules(modules);
        return this;
    }

    public JsonMapper findAndRegisterModules() {
        current.findAndRegisterModules();
        return this;
    }

    public SerializationConfig getSerializationConfig() {
        return current.getSerializationConfig();
    }

    public DeserializationConfig getDeserializationConfig() {
        return current.getDeserializationConfig();
    }

    public DeserializationContext getDeserializationContext() {
        return current.getDeserializationContext();
    }

    public SerializerFactory getSerializerFactory() {
        return current.getSerializerFactory();
    }

    public JsonMapper setSerializerFactory(SerializerFactory f) {
        current.setSerializerFactory(f);
        return this;
    }

    public SerializerProvider getSerializerProvider() {
        return current.getSerializerProvider();
    }

    public JsonMapper setSerializerProvider(DefaultSerializerProvider p) {
        current.setSerializerProvider(p);
        return this;
    }

    public SerializerProvider getSerializerProviderInstance() {
        return current.getSerializerProviderInstance();
    }

    public JsonMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins) {
        current.setMixIns(sourceMixins);
        return this;
    }

    public JsonMapper addMixIn(Class<?> target, Class<?> mixinSource) {
        current.addMixIn(target, mixinSource);
        return this;
    }

    public JsonMapper setMixInResolver(ClassIntrospector.MixInResolver resolver) {
        current.setMixInResolver(resolver);
        return this;
    }

    public Class<?> findMixInClassFor(Class<?> cls) {
        return current.findMixInClassFor(cls);
    }

    public int mixInCount() {
        return current.mixInCount();
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return current.getVisibilityChecker();
    }

    public JsonMapper setVisibility(VisibilityChecker<?> vc) {
        current.setVisibility(vc);
        return this;
    }

    public JsonMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility) {
        current.setVisibility(forMethod, visibility);
        return this;
    }

    public SubtypeResolver getSubtypeResolver() {
        return current.getSubtypeResolver();
    }

    public JsonMapper setSubtypeResolver(SubtypeResolver str) {
        current.setSubtypeResolver(str);
        return this;
    }

    public JsonMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        current.setAnnotationIntrospector(ai);
        return this;
    }

    public JsonMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI,
                                                 AnnotationIntrospector deserializerAI) {
        current.setAnnotationIntrospectors(serializerAI, deserializerAI);
        return this;
    }

    public PropertyNamingStrategy getPropertyNamingStrategy() {
        return current.getPropertyNamingStrategy();
    }

    public JsonMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        current.setPropertyNamingStrategy(s);
        return this;
    }

    public JsonMapper setSerializationInclusion(JsonInclude.Include incl) {
        current.setSerializationInclusion(incl);
        return this;
    }

    public JsonMapper setPropertyInclusion(JsonInclude.Value incl) {
        current.setPropertyInclusion(incl);
        return this;
    }

    public JsonMapper setDefaultPrettyPrinter(PrettyPrinter pp) {
        current.setDefaultPrettyPrinter(pp);
        return this;
    }

    public JsonMapper enableDefaultTyping() {
        current.enableDefaultTyping();
        return this;
    }

    public JsonMapper enableDefaultTyping(ObjectMapper.DefaultTyping dti) {
        current.enableDefaultTyping(dti);
        return this;
    }

    public JsonMapper enableDefaultTyping(ObjectMapper.DefaultTyping applicability, JsonTypeInfo.As includeAs) {
        current.enableDefaultTyping(applicability, includeAs);
        return this;
    }

    public JsonMapper enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping applicability, String propertyName) {
        current.enableDefaultTypingAsProperty(applicability, propertyName);
        return this;
    }

    public JsonMapper disableDefaultTyping() {
        current.disableDefaultTyping();
        return this;
    }

    public JsonMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        current.setDefaultTyping(typer);
        return this;
    }

    public void registerSubtypes(Class<?>... classes) {
        current.registerSubtypes(classes);
    }

    public void registerSubtypes(NamedType... types) {
        current.registerSubtypes(types);
    }

    public TypeFactory getTypeFactory() {
        return current.getTypeFactory();
    }

    public JsonMapper setTypeFactory(TypeFactory f) {
        current.setTypeFactory(f);
        return this;
    }

    public JavaType constructType(Type t) {
        return current.constructType(t);
    }

    public JsonNodeFactory getNodeFactory() {
        return current.getNodeFactory();
    }

    public JsonMapper setNodeFactory(JsonNodeFactory f) {
        current.setNodeFactory(f);
        return this;
    }

    public JsonMapper addHandler(DeserializationProblemHandler h) {
        current.addHandler(h);
        return this;
    }

    public JsonMapper clearProblemHandlers() {
        current.clearProblemHandlers();
        return this;
    }

    public JsonMapper setConfig(DeserializationConfig config) {
        current.setConfig(config);
        return this;
    }

    public JsonMapper setFilterProvider(FilterProvider filterProvider) {
        current.setFilterProvider(filterProvider);
        return this;
    }

    public JsonMapper setBase64Variant(Base64Variant v) {
        current.setBase64Variant(v);
        return this;
    }

    public JsonMapper setConfig(SerializationConfig config) {
        current.setConfig(config);
        return this;
    }

    public JsonFactory getFactory() {
        return current.getFactory();
    }

    public DateFormat getDateFormat() {
        return current.getDateFormat();
    }

    public JsonMapper setDateFormat(DateFormat dateFormat) {
        current.setDateFormat(dateFormat);
        return this;
    }

    public Object setHandlerInstantiator(HandlerInstantiator hi) {
        return current.setHandlerInstantiator(hi);
    }

    public InjectableValues getInjectableValues() {
        return current.getInjectableValues();
    }

    public JsonMapper setInjectableValues(InjectableValues injectableValues) {
        current.setInjectableValues(injectableValues);
        return this;
    }

    public JsonMapper setLocale(Locale l) {
        current.setLocale(l);
        return this;
    }

    public JsonMapper setTimeZone(TimeZone tz) {
        current.setTimeZone(tz);
        return this;
    }

    public boolean isEnabled(MapperFeature f) {
        return current.isEnabled(f);
    }

    public JsonMapper configure(MapperFeature f, boolean state) {
        current.configure(f, state);
        return this;
    }

    public JsonMapper enable(MapperFeature... f) {
        current.enable(f);
        return this;
    }

    public JsonMapper disable(MapperFeature... f) {
        current.disable(f);
        return this;
    }

    public boolean isEnabled(SerializationFeature f) {
        return current.isEnabled(f);
    }

    public JsonMapper configure(SerializationFeature f, boolean state) {
        current.configure(f, state);
        return this;
    }

    public JsonMapper enable(SerializationFeature f) {
        current.enable(f);
        return this;
    }

    public JsonMapper enable(SerializationFeature first, SerializationFeature... f) {
        current.enable(first, f);
        return this;
    }

    public JsonMapper disable(SerializationFeature f) {
        current.disable(f);
        return this;
    }

    public JsonMapper disable(SerializationFeature first, SerializationFeature... f) {
        current.disable(first, f);
        return this;
    }

    public boolean isEnabled(DeserializationFeature f) {
        return current.isEnabled(f);
    }

    public JsonMapper configure(DeserializationFeature f, boolean state) {
        current.configure(f, state);
        return this;
    }

    public JsonMapper enable(DeserializationFeature feature) {
        current.enable(feature);
        return this;
    }

    public JsonMapper enable(DeserializationFeature first, DeserializationFeature... f) {
        current.enable(first, f);
        return this;
    }

    public JsonMapper disable(DeserializationFeature feature) {
        current.disable(feature);
        return this;
    }

    public JsonMapper disable(DeserializationFeature first, DeserializationFeature... f) {
        current.disable(first, f);
        return this;
    }

    public boolean isEnabled(JsonParser.Feature f) {
        return current.isEnabled(f);
    }

    public JsonMapper configure(JsonParser.Feature f, boolean state) {
        current.configure(f, state);
        return this;
    }

    public JsonMapper enable(JsonParser.Feature... features) {
        current.enable(features);
        return this;
    }

    public JsonMapper disable(JsonParser.Feature... features) {
        current.disable(features);
        return this;
    }

    public boolean isEnabled(JsonGenerator.Feature f) {
        return current.isEnabled(f);
    }

    public JsonMapper configure(JsonGenerator.Feature f, boolean state) {
        current.configure(f, state);
        return this;
    }

    public JsonMapper enable(JsonGenerator.Feature... features) {
        current.enable(features);
        return this;
    }

    public JsonMapper disable(JsonGenerator.Feature... features) {
        current.disable(features);
        return this;
    }

    public boolean isEnabled(JsonFactory.Feature f) {
        return current.isEnabled(f);
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(jp, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonParser jp, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(jp, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public final <T> T readValue(JsonParser jp, ResolvedType valueType) throws JsonException {
        try {
            return current.readValue(jp, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws JsonException {
        try {
            return current.readValue(jp, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T extends TreeNode> T readTree(JsonParser jp) throws JsonException {
        try {
            return current.readTree(jp);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) throws JsonException {
        try {
            return current.readValues(p, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) throws JsonException {
        try {
            return current.readValues(p, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) throws JsonException {
        try {
            return current.readValues(p, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> MappingIterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValues(p, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(InputStream in) throws JsonException {
        try {
            return current.readTree(in);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(Reader r) throws JsonException {
        try {
            return current.readTree(r);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(String content) throws JsonException {
        try {
            return current.readTree(content);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(byte[] content) throws JsonException {
        try {
            return current.readTree(content);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(File file) throws JsonException {
        try {
            return current.readTree(file);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonNode readTree(URL source) throws JsonException {
        try {
            return current.readTree(source);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(JsonGenerator g, Object value) throws JsonException {
        try {
            current.writeValue(g, value);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeTree(JsonGenerator jgen, TreeNode rootNode) throws JsonException {
        try {
            current.writeTree(jgen, rootNode);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws JsonException {
        try {
            current.writeTree(jgen, rootNode);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public ObjectNode createObjectNode() {
        return current.createObjectNode();
    }

    public ArrayNode createArrayNode() {
        return current.createArrayNode();
    }

    public JsonParser treeAsTokens(TreeNode n) {
        return current.treeAsTokens(n);
    }

    public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonException {
        try {
            return current.treeToValue(n, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T extends JsonNode> T valueToTree(Object fromValue) throws JsonException {
        try {
            return current.valueToTree(fromValue);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return current.canSerialize(type);
    }

    public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
        return current.canSerialize(type, cause);
    }

    public boolean canDeserialize(JavaType type) {
        return current.canDeserialize(type);
    }

    public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause) {
        return current.canDeserialize(type, cause);
    }

    public <T> T readValue(File src, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(File src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(src, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(File src, JavaType valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(URL src, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(URL src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(src, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(URL src, JavaType valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(String content, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(content, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(content, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(String content, JavaType valueType) throws JsonException {
        try {
            return current.readValue(content, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(Reader src, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(Reader src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(src, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(Reader src, JavaType valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(InputStream src, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(src, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(InputStream src, JavaType valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws JsonException {
        try {
            return current.readValue(src, offset, len, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(src, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src, int offset, int len, TypeReference<T> valueTypeRef) throws JsonException {
        try {
            return current.readValue(src, offset, len, valueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src, JavaType valueType) throws JsonException {
        try {
            return current.readValue(src, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws JsonException {
        try {
            return current.readValue(src, offset, len, valueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(File resultFile, Object value) throws JsonException {
        try {
            current.writeValue(resultFile, value);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(OutputStream out, Object value) throws JsonException {
        try {
            current.writeValue(out, value);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void writeValue(Writer w, Object value) throws JsonException {
        try {
            current.writeValue(w, value);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public String writeValueAsString(Object value) throws JsonException {
        try {
            return current.writeValueAsString(value);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public byte[] writeValueAsBytes(Object value) throws JsonException {
        try {
            return current.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public JsonWriter writer() {
        return new JsonWriter(current.writer());
    }

    public JsonWriter writer(SerializationFeature feature) {
        return new JsonWriter(current.writer(feature));
    }

    public JsonWriter writer(SerializationFeature first, SerializationFeature... other) {
        return new JsonWriter(current.writer(first, other));
    }

    public JsonWriter writer(DateFormat df) {
        return new JsonWriter(current.writer(df));
    }

    public JsonWriter writerWithView(Class<?> serializationView) {
        return new JsonWriter(current.writerWithView(serializationView));
    }

    public JsonWriter writerFor(Class<?> rootType) {
        return new JsonWriter(current.writerFor(rootType));
    }

    public JsonWriter writerFor(TypeReference<?> rootType) {
        return new JsonWriter(current.writerFor(rootType));
    }

    public JsonWriter writerFor(JavaType rootType) {
        return new JsonWriter(current.writerFor(rootType));
    }

    public JsonWriter writer(PrettyPrinter pp) {
        return new JsonWriter(current.writer(pp));
    }

    public JsonWriter writerWithDefaultPrettyPrinter() {
        return new JsonWriter(current.writerWithDefaultPrettyPrinter());
    }

    public JsonWriter writer(FilterProvider filterProvider) {
        return new JsonWriter(current.writer(filterProvider));
    }

    public JsonWriter writer(FormatSchema schema) {
        return new JsonWriter(current.writer(schema));
    }

    public JsonWriter writer(Base64Variant defaultBase64) {
        return new JsonWriter(current.writer(defaultBase64));
    }

    public JsonWriter writer(CharacterEscapes escapes) {
        return new JsonWriter(current.writer(escapes));
    }

    public JsonWriter writer(ContextAttributes attrs) {
        return new JsonWriter(current.writer(attrs));
    }

    public JsonReader reader() {
        return new JsonReader(current.reader());
    }

    public JsonReader reader(DeserializationFeature feature) {
        return new JsonReader(current.reader(feature));
    }

    public JsonReader reader(DeserializationFeature first, DeserializationFeature... other) {
        return new JsonReader(current.reader(first, other));
    }

    public JsonReader readerForUpdating(Object valueToUpdate) {
        return new JsonReader(current.readerForUpdating(valueToUpdate));
    }

    public JsonReader readerFor(JavaType type) {
        return new JsonReader(current.readerFor(type));
    }

    public JsonReader readerFor(Class<?> type) {
        return new JsonReader(current.readerFor(type));
    }

    public JsonReader readerFor(TypeReference<?> type) {
        return new JsonReader(current.readerFor(type));
    }

    public JsonReader reader(JsonNodeFactory f) {
        return new JsonReader(current.reader(f));
    }

    public JsonReader reader(FormatSchema schema) {
        return new JsonReader(current.reader(schema));
    }

    public JsonReader reader(InjectableValues injectableValues) {
        return new JsonReader(current.reader(injectableValues));
    }

    public JsonReader readerWithView(Class<?> view) {
        return new JsonReader(current.readerWithView(view));
    }

    public JsonReader reader(Base64Variant defaultBase64) {
        return new JsonReader(current.reader(defaultBase64));
    }

    public JsonReader reader(ContextAttributes attrs) {
        return new JsonReader(current.reader(attrs));
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) throws JsonException {
        try {
            return current.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) throws JsonException {
        try {
            return current.convertValue(fromValue, toValueTypeRef);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public <T> T convertValue(Object fromValue, JavaType toValueType) throws JsonException {
        try {
            return current.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonException {
        try {
            current.acceptJsonFormatVisitor(type, visitor);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonException {
        try {
            current.acceptJsonFormatVisitor(type, visitor);
        } catch (Exception e) {
            throw new JsonException(e.getMessage(), e);
        }
    }
}
