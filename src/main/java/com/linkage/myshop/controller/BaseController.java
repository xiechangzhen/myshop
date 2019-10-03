package com.linkage.myshop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkage.myshop.controller.exception.FileEmptyException;
import com.linkage.myshop.controller.exception.FileSizeOutOfLimitException;
import com.linkage.myshop.controller.exception.FileTypeNotSupportException;
import com.linkage.myshop.controller.exception.FileUploadException;
import com.linkage.myshop.controller.exception.RequestException;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.AddressNotFoundException;
import com.linkage.myshop.service.exception.DeleteException;
import com.linkage.myshop.service.exception.DuplicateKeyException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.PasswordNotMatchException;
import com.linkage.myshop.service.exception.ServiceException;
import com.linkage.myshop.service.exception.UpdateException;
import com.linkage.myshop.service.exception.UsernameNotFoundException;
import com.linkage.myshop.service.util.ResponseResult;
/**
 * 所有控制器的基类
 * @author Administrator
 *
 */
public abstract class BaseController{
	public static final Integer SUCCESS = 200;
	
	/**
	 * 统一处理异常
	 * @param e 异常类型
	 * @return 返回JSON数据
	 */
	@ExceptionHandler({ServiceException.class,RequestException.class})
	@ResponseBody
	public ResponseResult<Void> handleException(Exception e){
		Integer state = null;
		//400 - 违反了Unique约束异常
		if(e instanceof DuplicateKeyException){
			state = 400;
		//401 -用户未找到	
		} else if(e instanceof UsernameNotFoundException){
			state = 401;
		//402 -密码不匹配异常	
		} else if(e instanceof PasswordNotMatchException){
			state = 402;
		//403 -地址未找到异常	
		} else if(e instanceof AddressNotFoundException){
			state = 403;
		//404 -访问权限异常异常			
		} else if(e instanceof AccessDenisedException){
			state = 404;
		//500 - 插入异常	
		} else if(e instanceof InsertException){
			state = 500;
		//501 - 更新异常	
		} else if(e instanceof UpdateException){
			state = 501;
		//502 - 删除异常	
		} else if(e instanceof DeleteException){
			state = 502;
		//600 - 上传文件为空异常
		} else if(e instanceof FileEmptyException){
			state = 600;
		//601 - 上传文件大小超出限制
		} else if(e instanceof FileSizeOutOfLimitException){
			state = 601;
		//602 - 上传文件类型不支持
		} else if(e instanceof FileTypeNotSupportException){
			state = 602;
		//610 - 上传文件类型不支持
		} else if(e instanceof FileUploadException){
			state = 610;
		} 
		return new ResponseResult<>(state,e);
	}
	
	/**
	 * 后续需要获取uid的应用场景还有很多，下面这个获取id的语法相对较长，可以把它封装在这，进行简化
	 * 能用方法进行转型的，都不用强制转换，因为方法以后有优化的可能，而强制转换则没有。
	 * @param session
	 * @return
	 */
	protected Integer getSessionId(HttpSession session) {
		return Integer.valueOf(session.getAttribute("uid").toString());
	}

}
