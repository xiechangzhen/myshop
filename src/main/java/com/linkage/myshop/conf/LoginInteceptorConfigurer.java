package com.linkage.myshop.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.linkage.myshop.interceptor.LoginInterceptor;

@Configuration
public class LoginInteceptorConfigurer implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 创建拦截器对象
		HandlerInterceptor interceptor = new LoginInterceptor();
		// 白名单，即：不要求登录即可访问的路径
//		String[] patterns = {"/js/**","/css/**","/images/**","/bootstrap3/**",
//				"/web/register.html","web/login.html","/user/reg.do","/user/login.do"};
		// 通过注册工具添加拦截器对象
		
		List<String> patterns = new ArrayList<>();
			patterns.add("/js/**");
			patterns.add("/css/**");
			patterns.add("/images/**");
			patterns.add("/bootstrap3/**");
			patterns.add("/web/register.html");
			patterns.add("/web/login.html");
			patterns.add("/goods/hot.do");
			patterns.add("/web/index.html");
			patterns.add("/user/login.do");
			patterns.add("/user/reg.do");
		registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(patterns);

		////			.addPathPatterns("/user/**") 
//			.addPathPatterns(patterns );
//		.excludePathPatterns("/user/login.do" ).excludePathPatterns("/user/reg.do");
		
		
	}


}
