package com.base.framework.core.exceptions;

/**
*  无效的业务参数异常
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
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
