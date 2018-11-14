package com.basics.framework.core.exceptions;

/**
 * @Description   : 安全异常
 * @Project       : framework-core
 * @Program Name  : com.yirun.framework.core.exception.SecurityExpception.java
 * @Author        : zc.ding@foxmail.com
 */
public class SecurityExpception extends BaseException {

	private static final long serialVersionUID = 8985547550244289493L;

	public SecurityExpception() {
		super();
	}

	public SecurityExpception(String message) {
		super(message);
	}

	public SecurityExpception(Throwable cause) {
		super(cause);
	}

	public SecurityExpception(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityExpception(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
