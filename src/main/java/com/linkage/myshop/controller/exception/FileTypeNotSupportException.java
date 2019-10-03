package com.linkage.myshop.controller.exception;
/**
 * 上传文件类型不支持
 * @author Administrator
 *
 */
public class FileTypeNotSupportException extends FileUploadException{
	private static final long serialVersionUID = -7345687570380412491L;

	public FileTypeNotSupportException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileTypeNotSupportException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FileTypeNotSupportException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FileTypeNotSupportException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FileTypeNotSupportException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
