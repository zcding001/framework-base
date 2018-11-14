package com.base.framework.core.exceptions;

/**
 * @Description   : 无效的业务参数异常
 * @Project       : framework-core
 * @Program Name  : com.yirun.framework.core.exception.InvalidBusinessArgumentException.java
 * @Author        : zc.ding@foxmail.com
 */
public class InvalidBusinessArgumentException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public InvalidBusinessArgumentException() {
		super();
	}

	public InvalidBusinessArgumentException(String message) {
		super(message);
	}

	public InvalidBusinessArgumentException(Throwable cause) {
		super(cause);
	}

	public InvalidBusinessArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBusinessArgumentException(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
