package com.linkage.myshop.service.exception;
/**
 * 访问权限异常
 * @author Administrator
 *
 */
public class AccessDenisedException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6452865373069248263L;

	public AccessDenisedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccessDenisedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AccessDenisedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AccessDenisedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AccessDenisedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
