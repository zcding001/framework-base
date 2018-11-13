package com.base.framework.core.annotions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*  防重复提交TOKEN
*  @date                    ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	
	/**
	 * @Description   : token的操作类别（Operate）
	 * @Project       : framework-core
	 * @Program Name  : com.yirun.framework.core.annotation.Token.java
	 * @Author        : zc.ding@foxmail.com
	 */
	public enum Ope{
		/**
		 * 添加token
		 */
		ADD,
		/**
		 * 验证、刷新token
		 */
		REFRESH,
		/**
		 * 验证、删除token
		 */
		REMOVE
	}

	/**
	 * 操作类型 ，推荐使用1,2<br/>
	 * 		ADD:add <br>
	 * 		REFRESH:validate and refresh<br>
	 * 		REMOVE:validate and remove <br>
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @return
	 */
	Ope operate() default Ope.REFRESH;
}
