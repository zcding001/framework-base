package com.basics.framework.core.exceptions;

/**
 * @Description   : 普通异常
 * @Project       : framework-core
 * @Program Name  : com.yirun.framework.core.exception.GeneralException.java
 * @Author        : zc.ding@foxmail.com
 */
public class GeneralException extends BaseException {

	private static final long serialVersionUID = 7907045998462038823L;

	public GeneralException() {
		super();
	}

	public GeneralException(String message) {
		super(message);
	}

	public GeneralException(Throwable cause) {
		super(cause);
	}

	public GeneralException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralException(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
