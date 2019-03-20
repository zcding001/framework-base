package com.base.framework.core.exceptions;

/**
*  基类异常，只能通过继承使用
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 3081005071365603850L;

	/**
	 * 错误编码
	 */
	private String code;

	private Object[] values;

	/**
	 * 错误消息
	 */
	private String errorMsg;

	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message, Throwable cause, String code, Object[] values) {
		super(message, cause);
		this.code = code;
		this.values = values;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object[] getValues() {
		return this.values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}