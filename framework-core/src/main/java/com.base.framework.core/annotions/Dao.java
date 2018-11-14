package com.base.framework.core.annotions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * @Description   : DAO bean注入
 * @Project       : framework-core
 * @Program Name  : com.yirun.framework.core.annotation.Dao.java
 * @Author        : zc.ding@foxmail.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Dao {

    String value() default "";

    // 数据源KEY
    String[] dsKey() default {};
}
