package com.linkage.myshop.service.util;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable{
	private static final long serialVersionUID = -2018872188314063323L;

	private Integer state ;
	private String message;
	private T data;
	
	public ResponseResult() {
		super();
	}
	
	public ResponseResult(Integer state) {
		super();
		setState(state);
	}

	public ResponseResult(Integer state, String message) {
		this(state);
		setMessage(message);
	}
	public ResponseResult(Integer state, Exception e) {
		this(state);
		setMessage(e.getMessage());
	}
	public ResponseResult(Integer state, T data) {
		this(state);
		setData(data);
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
