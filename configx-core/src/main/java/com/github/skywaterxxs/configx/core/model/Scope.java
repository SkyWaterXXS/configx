package com.github.skywaterxxs.configx.core.model;

import java.lang.annotation.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.Scope</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 <pre>
 * 用来描述一个扩展是否多实例
 *
 * 如果一个服务接口标注了<code>@Scope(Option.PROTOTYPE)</code>，每次请求{@link AppExtensionLoader#getInstance(Class)} 均会返回一个新的实例
 *
 * 如果未标注<code>@Scope</code>则等值于<code>@Scope(Option.SINGLETON)</code>
 *
 * </pre>
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {

    /**
     * <pre>
     * 限定当前的服务是单例还是多例
     *
     * </pre>
     *
     * @return
     */
    Option value() default Option.SINGLETON;

    static enum Option {
        // 单例，一个容器中只有一个份实例
        SINGLETON,
        // 多例，一个容器中有多份实例，只有在请求时才会创建
        PROTOTYPE;
    }
}