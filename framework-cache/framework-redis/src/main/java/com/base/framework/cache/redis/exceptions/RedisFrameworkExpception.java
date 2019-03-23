package com.base.framework.cache.redis.exceptions;

import com.base.framework.core.exceptions.BaseException;

/**
*  redis自定义异常类
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class RedisFrameworkExpception extends BaseException {

	private static final long serialVersionUID = 1L;

	public RedisFrameworkExpception() {
		super();
	}

	public RedisFrameworkExpception(String message) {
		super(message);
	}

	public RedisFrameworkExpception(Throwable cause) {
		super(cause);
	}

	public RedisFrameworkExpception(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisFrameworkExpception(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
