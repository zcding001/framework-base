package com.basics.framework.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description   : JTA事务处理
 * @Project       : framework-core
 * @Program Name  : com.yirun.framework.core.annotations.JTATransaction.java
 * @Author        : zc.ding@foxmail.com
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JTATransaction {
}
