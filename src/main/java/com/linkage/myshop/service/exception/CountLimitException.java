package com.linkage.myshop.service.exception;
/**
 * 数量限制异常
 * @author Administrator
 *
 */
public class CountLimitException extends ServiceException {

	private static final long serialVersionUID = -2643395487099809965L;

	public CountLimitException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CountLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CountLimitException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CountLimitException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CountLimitException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
