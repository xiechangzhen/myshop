接口：interface
接口是一种规范，标准，是用于描述行为特征、行为模式的。
 
关于Spring的特性， 还有DI（Dependency Injection:依赖注入）和IOC（Inversion OF Control：控制反转），具体的表现就是：
使用Spring框架后，创建对象将由传统的new语法改变为通过Spring容器获取对象，这就是IOC（控制反转），其中，为了保证获取的对象中的
某些属性是已经被注入值，就需要使用到DI，具体的做法可以是通过set方式注入，或通过构造方法注入，或自动装配，所以，IOC，
是框架希望实现的目标，而DI是实现目标所使用的手段，也可以说：通过DI实现了IOC。

并且通过spring还可以快速的实现AOP(面向切面编程)，即：可以使得指定的某些数据处理流程（甚至所有处理流程）都会执行指定的方法。

在使用AOP时，需要添加相关依赖：aspectj-tools和aspect-weaver：
		<dependency>
			<groupId>aspect</groupId>
			<artifactId>aspectj-tools</artifactId>
			<version>1.0.6</version>
		</dependency>
		<dependency>
			<groupId>org.aspectj</groupId>
			<artifactId>com.springsource.org.aspectj.weaver</artifactId>
			<version>1.7.2</version>
		</dependency>

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
		@Around("execution(* cn.tedu.store.service.impl.*.*(..))")
		public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
			//在业务方法之前执行的代码
			long start = System.currentTimeMillis();
			
			//执行业务方法，可以表示任何一个应用范围之内
			Object obj = pjp.proceed();
			
			//在业务方法之后执行的代码
			long end = System.currentTimeMillis();
			System.err.println("执行时间："+(end-start));
			//如果返回类型时void则所有业务方法执行之后到这都是null，所以用Object接收
			return obj;
		}
	}

加载Spring-》 获取对象 -》测试登录-》释放资源
加载Spring-》 获取对象 -》测试注册-》释放资源

try{
	开启事务
	业务方法
	提交
}catch(RuntimeException e){
	回滚
}

还有测试类里的@Before 和@After 也是面向切面思想


###关于Spring，需要:
1.理解基本概念；
2.配置`<bean>`节点，及通过SET方式注入属性的值；
3.通过Spring读取`.properties`文件；
4.使用Spring表达式；
5.掌握`@Component0`、`@Service`、`@Controller`注解；
6.掌握`@Aspect`和`@Around`注解；
7.掌握`@Before`、和`@After`注解；
8.掌握`@Autowired` 和`@Resources` 注解；
9.理解自动装配中的`byName` 和`byType`；
10.理解Spring管理的对象的作用域、生命周期方法；

###复习-SpringMVC
SpringMVC的主要作用是：解决了自定义控制器如何接受请求，并给与响应的问题。

关于SpringMVC，需要：
1.记住执行流程图；
2.掌握`@Controller` 和`@RestController`注解；
3.掌握`@RequestMapping` 和`@GetMapping` 和`@PostMapping` 注解，及`@RequestMapping` 详细配置；
4.掌握`@RequestParam` 和 `PathVariable`  注解，及详细配置；
5.掌握`@ResponseBody`注解：
6.掌握`@ResponseBody`注解；
7.掌握转发数据的方式；
8.掌握转发与重定向；
9.掌握JSON数据格式；
10.理解Jackson框架；
11.掌握拦截器的使用；
12.掌握统一处理异常的做法，及`@ExceptionHandler`注解；

###复习-MyBatis
MyBatis的作用是：简化持久层开发，在开发项目时，不必自行编写JDBC相关代码，只需要定义接口的抽象方法及映射的SQL语句即可！
1.基本的使用；
2.获取新插入数据的id
3.掌握动态SQL中的`<if>`和`<foreach>`的使用；
4.掌握`<resultMap>`的使用。
5.掌握`@Param`注解。
6.理解实体类与VO类的区别。

###复习-SpringBoot
SpringBoot就是框架集合体。相对于传统的ssm框架，它的特点有：
1.没有web.xml配置文件；
2.没有Spring的配置文件；
3.自带Tomcat，并以Java项目的方式运行；

### 复习-项目
1.认识UUID；
2.认识MD5,并掌握摘要算法的引用（包括加盐）；
3.一次只解决一个问题，掌握拆分项目的方式：先拆分出数据模块，在拆分出各模块中的功能，再拆出各功能的开发步骤；
4.掌握自定义异常：所有业务错误都应该抛出异常，且是某中RuntimeException，便于统一处理，也便于事务回滚；
5.掌握事务；
6.认识RESTful。
7.掌握持久层、业务层、控制器层的开发步骤。

