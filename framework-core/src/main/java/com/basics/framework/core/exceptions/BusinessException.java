package com.basics.framework.core.exceptions;

/**
*  业务异常
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class BusinessException extends BaseException {

	private static final long serialVersionUID = -4796404349526245258L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}

}
