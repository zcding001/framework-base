package com.base.framework.zk.exceptions;

import com.base.framework.core.exceptions.BaseException;

/**
*  zk自定义异常类
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class ZkFrameworkExpception extends BaseException {

	private static final long serialVersionUID = 1L;

	public ZkFrameworkExpception() {
		super();
	}

	public ZkFrameworkExpception(String message) {
		super(message);
	}

	public ZkFrameworkExpception(Throwable cause) {
		super(cause);
	}

	public ZkFrameworkExpception(String message, Throwable cause) {
		super(message, cause);
	}

	public ZkFrameworkExpception(String message, Throwable cause, String code, Object[] values) {
		super(message, cause, code, values);
	}
}
