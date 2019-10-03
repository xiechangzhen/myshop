package com.linkage.myshop.service.exception;

public class UsernameNotFoundException extends ServiceException {
	private static final long serialVersionUID = 6830141666902007646L;

	public UsernameNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsernameNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UsernameNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UsernameNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UsernameNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
