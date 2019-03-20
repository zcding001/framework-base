package com.basics.framework.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
*  DAO bean注入
*  @since                   ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
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
