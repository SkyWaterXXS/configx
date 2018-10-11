package com.github.skywaterxxs.configx.core.model;

import java.lang.annotation.*;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.core.model.Name</p>
 * <p>描述:  </p>
 * <p>日期: 2018/9/10 </p>
 * <pre>
 * 用于标注一个实现，相当于给这个实现起了一个别名
 *
 * 可以使用 {@link HSFExtensionLoader#getInstance(Class, String)}，传递<code>@Name</code>指定的值来获取对应的扩展
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
public @interface Name {

    /**
     * <pre>
     * 扩展实现对应的名称
     *
     * </pre>
     *
     * @return
     */
    String value();
}

