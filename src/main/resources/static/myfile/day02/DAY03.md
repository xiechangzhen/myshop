### 8. 用户-登录-持久层

**1. 规划SQL语句**

登录涉及的SQL语句大致是：

	SELECT 
		uid,password,salt,is_delete,avatar,username
	FROM 
		t_user 
	WHERE 
		username=?

查询时，必须查询`uid`字段，因为当登录成功时，需要将`uid`存入Session；必须查询`password`字段，用于验证登录时密码是否正确；必须查询`salt`字段，用于将用户提交的原始密码进行加密，保证密码可以用于后续的登录验证；必须查询`is_delete`字段，如果该字段值表示“已删除”则应该拒绝登录；必须查询`avatar`字段，当登录成功时将其存储下来，后续需要显示头像时就可以直接显示，而不必再查数据库；还应该查询`username`字段，因为当用户注册时，假设使用的用户名是`ROOT`，则存储下来的就是`ROOT`，如果要查询，查询出来的也是`ROOT`，但是，该用户通过`root`、`Root`、`rOOt`都可以登录成功，如果使用登录时输入的用户名显示在界面上，就会导致显示不统一。

**2. 接口与抽象方法**

在开发“注册”功能时，已经完成“根据用户名查询用户数据”的功能，则直接使用该抽象方法`User findByUsername(String username)`即可，无须再次添加！

**3. 配置XML映射**

在开发“注册”功能时，已经完成映射的SQL语句，但是，查询的字段列表并不能满足此次“登录”的需求，则应该在原映射SQL语句的基础之上，添加查询更多的字段：

	<!-- 根据用户名查询用户数据 -->
	<!-- User findByUsername(String username) -->
	<select id="findByUsername"
		resultType="cn.tedu.store.entity.User">
		SELECT
			uid,
			password,salt,
			avatar,username,
			is_delete AS isDelete
		FROM
			t_user
		WHERE
			username=#{username}
	</select>

关于单元测试，也不用重新开发，直接使用此前的单元测试即可，如果能找到匹配用户名的用户数据，则查询结果中应该包含更多的信息！

### 9. 用户-登录-业务层

**1. 规划异常**

当用户登录时，如果输入的用户名不存在，则不允许登录，应该抛出`UserNotFoundException`；

当用户登录时，如果输入的密码错误，则不允许登录，应该抛出`PasswordNotMatchException`；

当用户登录时，如果`is_delete`被标记为1，表示“已删除”，则不允许登录，应该抛出`UserNotFoundException`；

则应该创建以上2个异常类，它们都应该继承自`ServiceException`，并添加来自父类的构造方法。

**2. 接口与抽象方法**

在`IUserService`接口中添加“登录”的抽象方法：

	User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException;

**3. 实现抽象方法**

在`UserServiceImpl`实现类中实现抽象方法：

	public User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
		// 根据参数username查询匹配的用户信息
		// 判断查询结果是否为null
		// 是：用户名对应的数据不存在，抛出UserNotFoundException

		// 判断isDelete值是否为1
		// 是：用户标记为“已删除”，抛出UserNotFoundException

		// 基于盐值和参数password执行加密
		// 判断加密后的密码与查询结果中的密码是否不匹配
		// 是：密码不匹配，抛出PasswordNotMatchException
		
		// 将查询结果中的salt, password, isDelete设置为null
		// 返回查询结果
	}

具体实现为：

	@Override
	public User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
		// 根据参数username查询匹配的用户信息
		User result = userMapper.findByUsername(username);
		
	    // 判断查询结果是否为null
		if (result == null) {
			// 是：用户名对应的数据不存在，抛出UserNotFoundException
			throw new UserNotFoundException(
				"登录失败，用户名不存在！");
		}

	    // 判断isDelete值是否为1
		if (result.getIsDelete() == 1) {
			// 是：用户标记为“已删除”，抛出UserNotFoundException
			throw new UserNotFoundException(
				"登录失败，用户名不存在！");
		}

	    // 基于盐值和参数password执行加密
		String salt = result.getSalt();
		String md5Password = getMd5Password(password, salt);
		
	    // 判断加密后的密码与查询结果中的密码是否不匹配
		if (!result.getPassword().equals(md5Password)) {
			// 是：密码不匹配，抛出PasswordNotMatchException
			throw new PasswordNotMatchException(
				"登录失败！密码错误！");
		}

	    // 将查询结果中的salt, password, isDelete设置为null
		result.setSalt(null);
		result.setPassword(null);
		result.setIsDelete(null);
	    // 返回查询结果
		return result;
	}

完成后，在`UserServiceTestCase`中编写并执行单元测试：

	@Test
	public void login() {
		try {
			String username = "digest";
			String password = "8888";
			User result = service.login(username, password);
			System.err.println(result);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 10. 用户-登录-控制器层

**1. 处理异常**

此次抛出了2个新的异常，则应该在`BaseController`中处理异常的方法中添加2个`else if`进行处理。

**2. 设计请求**

	请求路径：/users/login
	请求参数：String username, String password, HttpSession session
	请求类型：POST
	响应数据：ResponseResult<User>

**3. 处理请求**

在`UserController`类中添加处理请求的方法：

	@RequestMapping("login")
	public ResponseResult<User> login(String username, String password, HttpSession session) {
		// 调用业务层对象的login()方法执行登录
		// 将登录结果中的uid封装到session中
		// 将登录结果中的username封装到session中
		// 返回结果
	}

完成后，通过`http://localhost:8080/users/login?username=digest&password=8888`执行测试，测试完成后，将处理请求的方法的注解改为`@PostMapping`。

### 11. 用户-登录-前端页面

参考“注册”完成“登录”的页面功能。

注意：在`login.html`中，原有代码中登录按钮是`type="submit"`的，需要改为`type="button"`。

至此，注册和登录功能已经完成！但是，数据表中有许多错误数据，例如某些字段没有值，或密码没有加密。应该：

1. 删除用户表中所有数据；

2. 注册名为`root`且密码是`1234`的用户数据；

3. 自行创建2个或更多其他用户数据，用于后续的“修改密码”功能的测试。

### 12. 用户-修改密码-持久层

**1. 规划SQL语句**

执行修改密码的SQL语句大致是：

	UPDATE t_user SET password=?, modified_user=?, modified_time=? WHERE uid=?

在执行修改之前，还应该验证原密码是否正确，则还会涉及：

	SELECT password,salt FROM t_user WHERE uid=?

**2. 接口与抽象方法**

在`UserMapper.java`接口中添加抽象方法：

	Integer updatePassword(
		@Param("uid") Integer uid, 
		@Param("password") String password, 
		@Param("modifiedUser") String modifiedUser, 
		@Param("modifiedTime") Date modifiedTime);

	User findByUid(Integer uid);

**3. 配置XML映射**

在`UserMapper.xml`中配置以上2个抽象方法的映射：

	<!-- 更新用户的密码 -->
	<!-- Integer updatePassword(
			@Param("uid") Integer uid, 
			@Param("password") String password, 
			@Param("modifiedUser") String modifiedUser, 
			@Param("modifiedTime") Date modifiedTime) -->
	<update id="updatePassword">
		UPDATE
			t_user
		SET
			password=#{password},
			modified_user=#{modifiedUser},
			modified_time=#{modifiedTime}
		WHERE
			uid=#{uid}
	</update>
	
	<!-- 根据用户id查询用户数据 -->
	<!-- User findByUid(Integer uid) -->
	<select id="findByUid"
		resultType="cn.tedu.store.entity.User">
		SELECT
			password,salt
		FROM
			t_user
		WHERE
			uid=#{uid}
	</select>

在`UserMapperTestCase`中编写并执行单元测试，测试时，不要使用`root`用户进行测试：

	@Test
	public void updatePassword() {
		Integer uid = 12;
		String password = "1234";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updatePassword(uid, password, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void findByUid() {
		Integer uid = 12;
		User user = mapper.findByUid(uid);
		System.err.println(user);
	}

### 13. 用户-修改密码-业务层

**1. 规划异常**

当用户尝试修改密码时，应该先验证原密码是否正确，当原密码正确的情况下才允许执行修改密码。

为了保证能验证原密码，需要先查询用户数据，查询时，则可能出现用户数据不存在的问题（例如用户先登录，然后管理员删除了该用户的数据，然后用户尝试修改密码，就会出现这种情况），则将抛出`UserNotFoundException`，同理，还应该检查用户数据的`isDelete`是否正常，否则，还是应该抛出`UserNotFoundException`。

> 则对应的查询功能需要补充查询`is_delete`字段的值。

当用户数据是正确的情况下，则应该将原密码和盐结合起来加密，以验证原密码是否正确，如果不正确，则抛出`PasswordNotMatchException`。

最终，用户将执行`UPDATE`操作，则可能出现更新数据异常，抛出`UpdateException`。

以上可能抛出的3种异常中，`UpdateException`是需要新创建的，也是继承自`ServiceException`。

**2. 接口与抽象方法**

在`IUserService`接口中添加抽象方法：

	void changePassword(Integer uid, String oldPassword, String newPassword, String username) throws UserNotFoundException, PasswordNotMatchException, UpdateException;

**3. 实现抽象方法**

在`UserServiceImpl`类中重写该抽象方法：

	public void changePassword(Integer uid, String oldPassword, String newPassword, String username) throws UserNotFoundException, PasswordNotMatchException, UpdateException {
		// 根据参数uid查询用户数据

		// 判断查询结果是否为null
		// 是：抛出UserNotFoundException

		// 判断查询结果中的isDelete是否为1
		// 是：抛出UserNotFoundException

		// 取出查询结果中的盐值
		// 基于参数oldPassword和盐值执行加密
		// 判断加密结果与查询结果中的密码是否不匹配
		// 是：抛出PasswordNotMatchException

		// 基于参数newPassword和盐值执行加密
		// 创建当前时间对象，作为最后修改时间
		// 调用持久层执行更新密码（新密码是以上加密的结果），并获取返回值
		// 判断返回值是否不为1
		// 是：抛出UpdateException
	}

具体实现为：

	@Override
	public void changePassword(Integer uid, String oldPassword, String newPassword, String username)
			throws UserNotFoundException, PasswordNotMatchException, UpdateException {
		// 根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);

		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改密码错误！尝试访问的用户数据不存在！");
		}

		// 判断查询结果中的isDelete是否为1
		if (result.getIsDelete() == 1) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改密码错误！尝试访问的用户数据不存在！");
		}

		// 取出查询结果中的盐值
		String salt = result.getSalt();
		// 基于参数oldPassword和盐值执行加密
		String oldMd5Password = getMd5Password(oldPassword, salt);
		// 判断加密结果与查询结果中的密码是否不匹配
		if (!result.getPassword().equals(oldMd5Password)) {
			// 是：抛出PasswordNotMatchException
			throw new PasswordNotMatchException(
				"修改密码错误！原密码错误！");
		}

		// 基于参数newPassword和盐值执行加密
		String newMd5Password = getMd5Password(newPassword, salt);
		// 创建当前时间对象，作为最后修改时间
		Date now = new Date();
		// 调用持久层执行更新密码（新密码是以上加密的结果），并获取返回值
		Integer rows = userMapper.updatePassword(uid, newMd5Password, username, now);
		// 判断返回值是否不为1
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException(
				"修改密码错误！更新数据时发生未知错误！");
		}
	}

然后，在`UserServiceTestCase`中编写并执行单元测试：

	@Test
	public void changePassword() {
		try {
			Integer uid = 13;
			String username = "Admin";
			String oldPassword = "8888";
			String newPassword = "1234";
			service.changePassword(uid, oldPassword, newPassword, username);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
	}

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

### 16. 用户-修改资料-持久层

### 17. 用户-修改资料-业务层

### 18. 用户-修改资料-控制器层

### 19. 用户-修改资料-前端页面

### 20. 用户-上传头像-持久层

### 21. 用户-上传头像-业务层

### 22. 用户-上传头像-控制器层

### 23. 用户-上传头像-前端页面










