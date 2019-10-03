package com.linkage.myshop.controller.exception;
/**
 * 文件上传失败
 * @author Administrator
 *
 */
public class FileUploadException extends RequestException{
	private static final long serialVersionUID = -1455530593935580490L;

	public FileUploadException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FileUploadException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FileUploadException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
