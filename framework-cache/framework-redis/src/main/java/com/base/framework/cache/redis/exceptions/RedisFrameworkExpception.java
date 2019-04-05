package com.base.framework.cache.redis.exceptions;

/**
*  redis自定义异常类
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class RedisFrameworkExpception extends RuntimeException {

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
}
