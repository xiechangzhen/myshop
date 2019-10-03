package com.linkage.myshop.controller.exception;
/**
 * 请求异常，文件上传的基类
 * @author Administrator
 *
 */
public class RequestException extends RuntimeException{
	private static final long serialVersionUID = 8287536289472589801L;

	public RequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}
