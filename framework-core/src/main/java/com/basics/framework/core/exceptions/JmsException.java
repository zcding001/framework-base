package com.basics.framework.core.exceptions;

/**
 * @Description   : JMS异常
 * @Project       : framework-core
 * @Program Name  : com.yirun.framework.core.exception.JmsException.java
 * @Author        : zc.ding@foxmail.com
 */
public class JmsException extends BaseException {

	private static final long serialVersionUID = 6166805485791565894L;

	public JmsException() {
		super();
	}

	public JmsException(String message) {
		super(message);
	}

	public JmsException(Throwable cause) {
		super(cause);
	}

	public JmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public JmsException(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}

}
