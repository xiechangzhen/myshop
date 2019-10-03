### 14. 用户-修改密码-控制器层

**1. 处理异常**

此次抛出了新的异常`UpdateException`，需要在`BaseController`中添加1个新的`else if`对其进行处理。

**2. 设计请求**

	请求路径：/users/change_password
	请求参数：@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword, HttpSession session
	请求类型：POST
	响应数据：ResponseResult<Void>

**3. 处理请求**

在`UserController`中添加处理请求的方法：

	@RequestMapping("change_password")
	public ResponseResult<Void> changePassword(
		@RequestParam("old_password") String oldPassword,
		@RequestParam("new_password") String newPassword, 
		HttpSession session) {
		// 从session中获取uid
		// 从session中获取username
		// 调用业务层对象执行修改密码
		// 返回操作成功
	}

完成后，先登录，通过`http://localhost:8080/users/change_password?old_password=1234&new_password=8888`进行测试，测试完成后，将`@RequestMapping`修改为`@PostMapping`。

### 15. 用户-修改密码-前端页面

关于AJAX请求与HTML代码的处理，与前序操作相同。

### 补：拦截器

创建`cn.tedu.store.interceptor.LoginInterceptor`登录拦截器类，实现`HandlerInterceptor`接口，并重写`preHandle()`方法，根据Session中有没有uid来决定拦截或放行：

	public class LoginInterceptor implements HandlerInterceptor {

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
				response.sendRedirect("/web/login.html");
				return false;
			}
			// 放行
			return true;
		}
	
	}

然后，在`cn.tedu.store.conf`包下创建`LoginInterceptorConfigurer`类，作为配置拦截器的类，需要添加`@Configuration`注解，并实现`WebMvcConfigurer`接口（使用早期版本的话是继承自`WebMvcConfigurerAdapter`），并重写`addInterceptors()`方法以完成拦截器的配置：

	@Configuration
	public class LoginInterceptorConfigurer 
		implements WebMvcConfigurer {
	
		@Override
		public void addInterceptors(
				InterceptorRegistry registry) {
			// 创建拦截器对象
			HandlerInterceptor interceptor
				= new LoginInterceptor();
			
			// 白名单，即：不要求登录即可访问的路径
			List<String> patterns = new ArrayList<>();
			patterns.add("/js/**");
			patterns.add("/css/**");
			patterns.add("/images/**");
			patterns.add("/bootstrap3/**");
			patterns.add("/web/register.html");
			patterns.add("/web/login.html");
			patterns.add("/users/reg");
			patterns.add("/users/login");
			
			// 通过注册工具添加拦截器对象
			registry.addInterceptor(interceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(patterns );
		}
	
	}

当添加了拦截器后，如果在浏览器中，打开了“修改密码”页，登录超时后再点击“修改”按钮，会没有反应！因为是通过AJAX发出的异步请求，由于登录超时，服务器端的拦截器会要求重定向，浏览器是通过AJAX对应的子线程去完成重定向的，而在浏览器窗体中没有任何体现！所以，需要在调用的`$.ajax()`函数的参数中，补充`error`属性，该属性会在服务器响应非2xx响应码时被触发执行其回调函数：

	<script type="text/javascript">
	$("#btn-change-password").click(function(){
		$.ajax({
			"url":"/users/change_passwordAAAAAA",
			"data":$("#form-change-password").serialize(),
			"type":"post",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("修改成功！");
				} else {
					alert(json.message);
				}
			},
			"error":function() {
				alert("您的登录信息已过期，请重新登录！");
				location.href="/web/login.html";
			}
		});
	});
	</script>

> 其实，服务器端响应400、404、405、500等响应码，也会导致error的函数被回调，但是，完整的项目中，AJAX请求应该不会出现4xx的请求错误，服务器端也不应该会出现5xx的内部错误，所以，可以认为：完整的项目中，会导致error的函数被回调的，只有3xx的响应码。

### 16. 用户-修改资料-持久层

**1. 规划SQL语句**

修改用户资料将使用的SQL语句大致是：

	UPDATE 
		t_user 
	SET 
		phone=?,email=?,
		gender=?,modified_user=?,
		modified_time=? 
	WHERE 
		uid=?

执行修改之前还是应该检查用户数据是否存在、是否被标记为删除，相关功能已经完成，无需再次开发。

在界面的处理方面，在用户点击“修改资料”之前，页面上应该已经显示当前的用户数据，即：需要查询出当前用户的相关数据：

	SELECT username,phone,email,gender FROM t_user WHERE uid=?

此前已经开发了`findByUid()`方法，则只需要补充查询的字段列表即可！

**2. 接口与抽象方法**

在`UserMapper.java`接口中添加抽象方法：

	Integer updateInfo(User user);

**3. 配置XML映射**

先配置映射：

	<!-- 更新用户个人资料 -->
	<!-- Integer updateInfo(User user) -->
	<update id="updateInfo">
		UPDATE
			t_user
		SET
			phone=#{phone},
			email=#{email},
			gender=#{gender},
			modified_user=#{modifiedUser},
			modified_time=#{modifiedTime}
		WHERE
			uid=#{uid}
	</update>

单元测试：

	@Test
	public void updateInfo() {
		User user = new User();
		user.setUid(14);
		user.setPhone("13800138014");
		user.setEmail("spring@tedu.cn");
		user.setGender(1);
		user.setModifiedUser("ROOT");
		user.setModifiedTime(new Date());
		Integer rows = mapper.updateInfo(user);
		System.err.println("rows=" + rows);
	}

### 17. 用户-修改资料-业务层

**1. 规划异常**

此次是对用户数据进行操作，应该先检查用户数据，则可能抛出`UserNotFoundException`。

此次执行是更新操作，则可能抛出`UpdateException`。

此次没有新的异常。

**2. 接口与抽象方法**

在`IUserService`接口中添加“修改资料”的抽象方法：

	void changeInfo(User user) throws UserNotFoundException, UpdateException;

**3. 实现抽象方法**

在`UserServiceImpl.java`中实现以上抽象方法：

	public void changeInfo(User user) throws UserNotFoundException, UpdateException {
		// 从参数user中获取uid
		// 调用持久层对象的方法，根据uid查询用户数据

		// 判断查询结果是否为null
		// 是：抛出UserNotFoundException

		// 判断查询结果中的isDelete是否为1
		// 是：抛出UserNotFoundException

		// 创建当前时间对象，封装到user中
		// TODO 确保modifiedUser属性是有值的

		// 调用持久层对象执行修改，并获取返回值（即受影响的行数）
		// 判断返回值是否不为1
		// 是：抛出UpdateException
	}

具体实现为：

	@Override
	public void changeInfo(User user) throws UserNotFoundException, UpdateException {
		// 从参数user中获取uid
		Integer uid = user.getUid();
		// 调用持久层对象的方法，根据uid查询用户数据
		User result = userMapper.findByUid(uid);

		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改个人资料错误！尝试访问的用户数据不存在！");
		}

		// 判断查询结果中的isDelete是否为1
		if (result.getIsDelete() == 1) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改个人资料错误！尝试访问的用户数据不存在！");
		}

		// 创建当前时间对象，封装到user中
		user.setModifiedTime(new Date());
		// TODO 确保modifiedUser属性是有值的

		// 调用持久层对象执行修改，并获取返回值（即受影响的行数）
		Integer rows = userMapper.updateInfo(user);
		// 判断返回值是否不为1
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException(
				"修改个人资料错误！更新数据时发生未知错误！");
		}
	}

完成后，编写并执行单元测试：

	@Test
	public void changeInfo() {
		try {
			User user = new User();
			user.setUid(11);
			user.setModifiedUser("超级管理员");
			user.setPhone("13800138011");
			user.setEmail("root@tedu.cn");
			user.setGender(1);
			service.changeInfo(user);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
	}

### 18. 用户-修改资料-控制器层

**1. 处理异常**

此次业务层没有抛出新的异常，抛出的都是已经在`BaseController`中处理过的类型的异常，所以，无需处理。

**2. 设计请求**

	请求路径：/users/change_info
	请求参数：User user, HttpSession session
	请求类型：POST
	响应数据：ResponseResult<Void>

**3. 处理请求**

在`UserController`中处理请求：

	@RequestMapping("change_info")
	public ResponseResult<Void> changeInfo(
		User user, HttpSession session) {
		// 获取uid
		Integer uid = getUidFromSession(session);
		// 获取用户名
		String username = session.getAttribute("username").toString();
		// 将uid和用户名封装到参数user对象中
		user.setUid(uid);
		user.setModifiedUser(username);
		// 调用业务层对象执行修改个人资料
		userService.changeInfo(user);
		// 返回操作成功
		return new ResponseResult<>(SUCCESS);
	}

完成后，在浏览器中通过`http://localhost:8080/users/change_info?phone=13900139011&email=root@qq.com&gender=0`执行测试。

### 19. 用户-修改资料-前端页面

与此前的页面不同，此次前端页面需要完成的功能有2项：

1. 当页面打开时，就显示当前登录的用户的信息；

2. 当点击“修改”按钮时，提交修改。

首先，完成第1项任务，应该是：当页面打开时，客户端（浏览器）就直接向服务器发出请求，请求获取当前登录的用户的信息，当服务器返回用户信息后，将这些数据显示在网页中。

目前，服务器端并不能响应客户端“请求获取当前登录的用户的信息”这样的需求，所以，需要在控制器中添加对这种请求的处理，但是，控制器中的功能都是通过业务层对象来实现的，而业务层中也没有这样功能，同理，业务层的功能是基于持久层实现的，而持久层已经有`findByUid()`方法可以实现，则持久层无需再开发。

所以，现在的任务应该是：

**开发“获取用户数据”的业务层**

应该先在业务层接口`IUserService`中添加新的抽象方法：

	User getByUid(Integer uid) throws UserNotFoundException;

然后在业务层实现类`UserServiceImpl`中实现以上方法：

	public User getByUid(Integer uid) throws UserNotFoundException {
		// 调用持久层对象查询参数uid匹配的用户数据
		// 判断查询结果是否为null
		// 判断查询结果中的isDelete是否为1
		// 将查询结果中的password/salt/isDelete设置为null
		// 返回查询结果
	}

具体实现为：

	@Override
	public User getByUid(Integer uid) throws UserNotFoundException {
		// 根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);

		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"获取用户信息失败！尝试访问的用户数据不存在！");
		}

		// 判断查询结果中的isDelete是否为1
		if (result.getIsDelete() == 1) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"获取用户信息失败！尝试访问的用户数据不存在！");
		}
		
		// 将查询结果中的password/salt/isDelete设置为null
		result.setPassword(null);
		result.setSalt(null);
		result.setIsDelete(null);
		
		// 返回查询结果
		return result;
	}

完成后，编写并执行单元测试：

	@Test
	public void getByUid() {
		try {
			Integer uid = 11;
			User user = service.getByUid(uid);
			System.err.println(user);
		} catch (ServiceException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
	}

**开发“获取用户数据”的控制器层**

设计请求：

	请求路径：/users/details
	请求参数：HttpSession session
	请求类型：GET/POST
	响应数据：ResponseResult<User>

处理请求：

	@GetMapping("details")
	public ResponseResult<User> getByUid(
			HttpSession session) {
		// 获取uid
		Integer uid = getUidFromSession(session);
		// 调用业务层对象执行获取数据
		User data = userService.getByUid(uid);
		// 返回操作成功及数据
		return new ResponseResult<>(SUCCESS, data);
	}

打开浏览器，先登录，然后通过`http://localhost:8080/users/details`测试。


核心：当界面刚刚加载时，即发出请求，获取当前登录界面完成初始化，即前端的HTML,CSS,JS已经解析完毕，此时，就可以发出请求
-将数据显示到各个控件中：为了方便操作，可以为各种控件配置id，则可以通过jQuery的ID选择器迅速选择到相关控件，然后，调用val(值)函数为控件设计值，
由于“性别”涉及到2个控件，不可以使用相同的id值，所以需要分别为2个控件设计不同的id，例如：gender-male和gender-female，“性别”使用的是<input type="radio">标签，
这种控件的选中是依赖于checked="checked"属性，所以，当需要通过JavaScript代码控件选中时，就是为其中的某个控件设置该属性即可，

**关于登录超时后的访问**
假设用户已经登录，但长时间未操作导致Session超时，然后，再点击界面的按钮执行“修改资料”或“修改密码”，界面的表现是**没有任何变化**！主要原因是：
异步请求可以理解为子线程发出的请求，由于登录超时，服务器端的拦截器会要求重定向到登录页，而获取到重定向这个响应结果的是子线程，主线程完全不知道这个过程，所以，界面上没有任何变化！

在ajax()函数中，如果服务器端响应的不是正确的响应码，而是3xx、4xx、5xx，需要 通过error进行配置，例如：

		<script type="text/javascript">
		//将页面初始化时加载用户数据
			$(document).ready(function (){
				//将请求提交到哪里
				var url = "../user/show_user_data.do";
				//发出ajax请求，并处理结果
					$.ajax({
						"url":url,
						"type":"POST",
						"dataType":"json",
						"success":function(json){
							if(json.state == 200){
								var user = json.data;
								$("#username").val(user.username);
								$("#phone").val(user.phone);
								$("#email").val(user.email);
								if(user.gender==1){
									$("#nv").attr("checked","checked");
								}else if(user.gender==0){
									$("#nan").attr("checked","checked");
								}
							} else{
								alert("信息显示异常");
							}
						}
				});
			})
			
			$("#btn_user_data").click(function(){
				var url = "../user/change_Info1.do";
				var data = $("#form_user_data").serialize();
				console.log("用户信息数据："+data);
				$.ajax({
					"url":url,
					"data":data,
					"type":"POST",
					"dataType":"json",
					"success":function(json){
						if(json.state == 200){
							alert("用户信息修改成功");
							location.href="index.html";
						} else if(json.state ==401 ){
						 	alert(json.message);
						} else if(json.state == 402){
							alert(json.message);
						} else{
							alert("用户信息修改异常");
						}
						
					},
					//"error":function()){
					//"error":function(XMLHttpRequest)){ 
					"error":function(xhr, text, errorThrown){
	 				//"error":function(){
						alert("XMLHttpRequest.readyState="+ xhr.readyState);
						alert("xhr.status="+ xhr.status);
	 					alert("您的登录信息已经过期！请重新登录");
						location.href="login.html";
					} 
				
				});
			})	
		</script>

由于4xx错误都是请求出错，例如请求的URL不存在，请求类型错误，请求参数不足等，而请求时开发者编写的，理论上来说，完整的项目中应该不会出现4xx错误。
而5xx是服务器的程序运行出错，是更不应该出现的！
所以，通常导致error的大多是重定向，则处理方式：



**在前端页面中，打开时即请求当前登录的用户数据，并显示**

**在前端页面中，当点击“修改”按钮时执行修改个人资料**

### 20. 用户-上传头像-持久层

### 21. 用户-上传头像-业务层

### 22. 用户-上传头像-控制器层

### 23. 用户-上传头像-前端页面
