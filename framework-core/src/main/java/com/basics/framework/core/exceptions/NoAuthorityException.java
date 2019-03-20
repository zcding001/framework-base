package com.basics.framework.core.exceptions;

/**
*  无权限异常
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class NoAuthorityException extends SecurityExpception {

	private static final long serialVersionUID = -3028496821010699394L;

	public NoAuthorityException() {
		super();
	}

	public NoAuthorityException(String message) {
		super(message);
	}

	public NoAuthorityException(Throwable cause) {
		super(cause);
	}

	public NoAuthorityException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoAuthorityException(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
