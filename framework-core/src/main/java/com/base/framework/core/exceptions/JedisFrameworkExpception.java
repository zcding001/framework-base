package com.base.framework.core.exceptions;

/**
*  redis自定义异常类
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class JedisFrameworkExpception extends BaseException {

	private static final long serialVersionUID = 1L;

	public JedisFrameworkExpception() {
		super();
	}

	public JedisFrameworkExpception(String message) {
		super(message);
	}

	public JedisFrameworkExpception(Throwable cause) {
		super(cause);
	}

	public JedisFrameworkExpception(String message, Throwable cause) {
		super(message, cause);
	}

	public JedisFrameworkExpception(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
