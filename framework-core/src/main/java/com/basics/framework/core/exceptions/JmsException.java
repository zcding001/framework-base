package com.basics.framework.core.exceptions;

/**
*  JMS异常
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
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
