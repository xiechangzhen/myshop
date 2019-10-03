package com.linkage.myshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 获取session对象
		HttpSession session = request.getSession();
		// 尝试从session中获取用户的uid（因为登录后session中肯定有uid）
		Object uid = session.getAttribute("uid");
		// 判断是否正确的获取到了uid
		if (uid == null) {
			// 尝试获取uid失败，意味着用户没有登录，或登录已超
			System.err.println("拦截器重定向"); 
			response.sendRedirect("../web/login.html");
			System.out.println("redirect");
			return false;
		}
		// 放行
		return true;
	}


}
