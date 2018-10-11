package com.github.skywaterxxs.configx.core.model;

import java.lang.annotation.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.Shared</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 * <pre>
 * 用来描述一个服务接口类型，被标注的服务是一个共享服务
 *
 *        Shared Container
 *              |
 *              |
 *   -----------------------
 *   |      |      |       |
 *   |      |      |       |
 * Sub1    Sub2   Sub2    Sub3
 *
 * 如果标注了<code>@Shared</code>，表示该服务的实例将放置在Shared Container中
 *
 * 每个Sub容器都具备自己的服务实例，在一个{@link AppExtensionLoader}实例中，服务单例与多例，需要再看{@link Scope}
 * </pre>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Shared {
    // 只是一个Marker注解
}
