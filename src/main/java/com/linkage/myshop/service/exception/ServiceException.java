package com.linkage.myshop.service.exception;
/**
 * 业务异常，是当前项目中所有业务异常的基类
 * @author Administrator
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -1448131418861627145L;

	public ServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
