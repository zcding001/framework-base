package com.base.framework.zk.exceptions;

/**
*  zk自定义异常类
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class ZkFrameworkExpception extends RuntimeException {

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
}
