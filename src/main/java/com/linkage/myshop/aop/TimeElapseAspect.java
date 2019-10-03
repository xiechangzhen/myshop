package com.linkage.myshop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
/**
 * 面向切面演示
 * @author Administrator
 *
 */

//加上通用注解和面向切面注解
@Component
@Aspect
public class TimeElapseAspect {
	//ProceedingJoinPoint
	//环绕           执行的表达式    权限                         业务层实现类的所有类名  方法名   括号里的..包括有无参数                                            
//	@Around("execution(* cn.tedu.store.service.impl.*.*(..))")
	@Around("execution(* com.linkage.myshop.service.impl.*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
		//在业务方法之前执行的代码
		long start = System.currentTimeMillis();
		System.out.println("执行时间：before");		
		//执行业务方法，可以表示任何一个应用范围之内
		Object obj = pjp.proceed();
		
		//在业务方法之后执行的代码
		long end = System.currentTimeMillis();
		System.out.println("执行时间：after");
		System.err.println("执行时间："+(end-start));  
		//如果返回类型时void则所有业务方法执行之后到这都是null，所以用Object接收
		return obj;
	}
}
