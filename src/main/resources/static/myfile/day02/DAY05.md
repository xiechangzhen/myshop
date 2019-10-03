### 20. 用户-上传头像-持久层

**1. 规划SQL语句**

首先，上传头像的功能其实共分为2个部分，一部分是接收上传的文件并存储到服务器的硬盘中，另一部分是将存储下来的文件的路径记录在数据库中！所以，在此前`t_user`表的设计中，关于头像的`avatar`字段是使用`varchar(50)`，就是用来记录每个用户的头像的路径值的，例如`upload/xxxx.jpg`。

所以，对于持久层开发而言，上传头像需要做的事情的本质就是**修改当前用户的avatar字段的值**！则需要执行的SQL语句就是（与**修改密码**极为相似）：

	UPDATE t_user SET avatar=?, modified_user=?, modified_time=? WHERE uid=?

另外，在业务中，也应该检查用户数据是否存在、是否标记为已删除，这些功能已经存在，无需再开发。

**2. 接口与抽象方法**

在`UserMapper.java`接口中添加抽象方法：

	Integer updateAvatar(
		@Param("uid") Integer uid, 
		@Param("avatar") String avatar, 
		@Param("modifiedUser") String modifiedUser, 
		@Param("modifiedTime") Date modifiedTime);

**3. 配置XML映射**

在`UserMapper.xml`中配置以上抽象方法对应的SQL映射：

	<!-- 更新用户的头像 -->
	<!-- Integer updateAvatar(
			@Param("uid") Integer uid, 
			@Param("avatar") String avatar, 
			@Param("modifiedUser") String modifiedUser, 
			@Param("modifiedTime") Date modifiedTime) -->
	<update id="updateAvatar">
		UPDATE
			t_user
		SET
			avatar=#{avatar},
			modified_user=#{modifiedUser},
			modified_time=#{modifiedTime}
		WHERE
			uid=#{uid}
	</update>

在`UserMapperTestCase`中编写并执行单元测试：

	@Test
	public void updateAvatar() {
		Integer uid = 14;
		String avatar = "这里应该是头像的路径";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updateAvatar(uid, avatar, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}

### 21. 用户-上传头像-业务层

**1. 规划异常**

无

**2. 接口与抽象方法**

在`IUserService`接口中添加“上传头像”的抽象方法：

	void changeAvatar(Integer uid, String avatar, String username) throws UserNotFoundException, UpdateException;

**3. 实现抽象方法**

在`UserServiceImpl`实现类中实现以上抽象方法：

	public void changeAvatar(Integer uid, String avatar, String username) throws UserNotFoundException, UpdateException {
		// 根据参数uid查询用户数据
		// 判断用户数据是否为null，是，抛出异常
		// 判断用户数据中的isDelete是否为1，是，抛出异常

		// 创建当前时间对象
		// 调用持久层的方法执行更新头像，并获取返回值
		// 判断返回值是否不为1，是，抛出异常
	}

具体实现为：

	@Override
	public void changeAvatar(Integer uid, String avatar, String username)
			throws UserNotFoundException, UpdateException {
		// 根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);

		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改头像错误！尝试访问的用户数据不存在！");
		}

		// 判断查询结果中的isDelete是否为1
		if (result.getIsDelete() == 1) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException(
				"修改头像错误！尝试访问的用户数据不存在！");
		}
		
		// 创建当前时间对象
		Date now = new Date();
		// 调用持久层的方法执行更新头像，并获取返回值
		Integer rows = userMapper.updateAvatar(uid, avatar, username, now);
		// 判断返回值是否不为1，是，抛出异常
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException(
				"修改头像错误！更新数据时发生未知错误！");
		}
	}

完成后，在`UserServiceTestCase`中编写并执行单元测试：

	@Test
	public void changeAvatar() {
		try {
			Integer uid = 14;
			String username = "Admin";
			String avatar = "新头像地址";
			service.changeAvatar(uid, avatar, username);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
	}

### 22. 用户-上传头像-控制器层

**1. 处理异常**

暂无

**2. 设计请求**

	请求路径：/users/change_avatar
	请求参数：HttpServletRequest request, @RequestParam("avatar") MultipartFile avatar, HttpSession session
	请求类型：POST
	响应数据：ResponseResult<String>

**3. 处理请求**

	@PostMapping("change_avatar")
	public ResponseResult<String> changeAvatar(
		HttpServletRequest request, 
		@RequestParam("avatar") MultipartFile avatar, 
		HttpSession session) {
		// TODO 判断isEmpty
		// TODO 判断文件大小：SpringBoot默认不支持上传大文件
		// TODO 判断文件类型

		// 确定保存文件的文件夹的File对象：参考上传的demo
		// 确定保存的文件的文件名：参考上传的demo
		// 创建保存文件的File对象：dest = new File(parent, child);
		// 执行上传并保存头像文件：avatar.transferTo(dest);

		// 从Session中获取uid和username
		// 执行修改数据：service.changeAvatar(uid, avatarPath, username)

		// 返回成功及avatarPath
	}

### 23. 用户-上传头像-前端页面



### 附：1. 基于SpringMVC的文件上传

#### 1.1. 创建项目

创建新的SpringMVC的项目，即：创建**Maven Project**，**Group Id**是`cn.tedu.spring`，**Artifact Id**是`SPRINGMVC-03-UPLOAD`，然后创建出项目。

#### 1.2. 添加必要的依赖

本项目需要`spring-webmvc`依赖，另，上传功能需要`commons-fileupload`依赖：

	<dependency>
		<groupId>commons-fileupload</groupId>
		<artifactId>commons-fileupload</artifactId>
		<version>1.4</version>
	</dependency>

#### 1.3. 设计页面

在`webapp`下创建`index.html`静态页面，并在页面中添加上传的表单，上传控件是：

	<input type="file" />

> 以上标签还可以配置`multiple="multiple"`，用于批量上传，用户在选择上传的文件时，可以按住Ctrl键一6次选中多个文件最终提交。

实现文件上传时，`<form>`的`method`必须是`post`，并且，必须配置`enctype="multipart/form-data"`。

完整代码例如：

	<form action="" method="post" enctype="multipart/form-data">
		<p>请选择您要上传的文件</p>
		<p><input type="file" /></p>
		<p><input type="submit" value="上传" /><p>
	</form>

#### 1.4. Spring配置

在实现上传之前，需要在Spring的配置文件添加配置：

	<bean id="multipartResolver"
		class="org.springframework.web.multipart.commons.CommonsMultipartResolver" />

在配置时，`<bean>`节点的`id`属性必须是`multipartResolver`。

该`<bean>`节点还可以用于为`CommonsMultipartResolver`的某些属性注入值，但是，不是必要的配置。

#### 1.5. 实现文件上传

用户通过网页可以选择要上传的文件，并提交到服务器端，服务器端需要使用控制器接收用户此次的提交数据的请求，并将数据存储到某个文件中，以完成文件上传。

首先，应该在服务器端创建控制器类`cn.tedu.spring.UploadController`（确保该类在组件扫描范围之内），添加`@Controller`注解，并添加处理请求的方法：

	@RequestMapping("upload.do")
	public String handleUpload() {
		return null;
	}

则前端页面的`<form>`的`action`属性值就必须是`upload.do`。

在控制器中，使用`MultipartFile`类型的参数，它表示客户端提交的文件的数据：

	@RequestMapping("upload.do")
	public String handleUpload(
			MultipartFile file) {
		return null;
	}

由于此处参数的名称是`file`，则前端页面的上传控件的`name`属性值也必须是`file`。

除此以外，还要求在该参数之前使用`@RequestParam("file")`注解：

	@RequestMapping("upload.do")
	public String handleUpload(
		@RequestParam("file") MultipartFile file) {
		return null;
	}

在处理请求的方法中，调用参数`MultipartFile file`的`transferTo(File dest)`方法即可将文件存储下来！

	@RequestMapping("upload.do")
	public String handleUpload(
		@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		File dest = new File("d:/ChengHeng/1.jpg");
		file.transferTo(dest);
		return null;
	}

#### 1.6. 确定上传文件的文件名

	@RequestMapping("upload.do")
	public String handleUpload(
		@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		// 确定上传文件将保存到哪个文件夹
		File parent = new File("d:/ChengHeng");
		
		// 获取上传的文件的原始名称
		String originalFilename = file.getOriginalFilename(); // DAY01.zip
		// 确定上传的文件的扩展名
		String suffix = "";
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex != -1) {
			suffix = originalFilename.substring(beginIndex);
		}
		// 确定上传的文件最终保存时使用的文件名
		String child = UUID.randomUUID().toString() + suffix;
		
		// 确定上传的文件最终保存时的文件对象
		File dest = new File(parent, child);
		// 执行保存
		file.transferTo(dest);
		return null;
	}

#### 1.7. 确定上传到的文件夹

	@RequestMapping("upload.do")
	public String handleUpload(
		HttpServletRequest request,
		@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		// 通过request对象获取项目部署到的文件夹中的某子级文件夹
		String parentPath 
			= request.getServletContext()
				.getRealPath("upload");
		// 确定上传文件将保存到哪个文件夹
		File parent = new File(parentPath);
		// 确保文件夹是存在的
		if (!parent.exists()) {
			parent.mkdirs();
		}
		
		// 获取上传的文件的原始名称
		String originalFilename = file.getOriginalFilename(); // DAY01.zip
		// 确定上传的文件的扩展名
		String suffix = "";
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex != -1) {
			suffix = originalFilename.substring(beginIndex);
		}
		// 确定上传的文件最终保存时使用的文件名
		String child = UUID.randomUUID().toString() + suffix;
		
		// 确定上传的文件最终保存时的文件对象
		File dest = new File(parent, child);
		// 执行保存
		file.transferTo(dest);
		return null;
	}

#### 1.8. 关于MutlipartFile中的常用方法

- `boolean isEmpty()`：判断上传的文件是否为空，当没有选择文件，或选择的文件是0字节时为`true`，否则为`false`。

- `String getOriginalFilename()`：获取文件的原始文件名，即在客户端中使用的文件名。

- `long getSize()`：获取文件的大小，以字节为单位。

- `String getContentType()`：获取文件的MIME类型，各扩展名与MIME类型的对应关系可以上网查阅，或在Tomcat的`conf/web.xml`中查找。

- `InputStream getInputStream()`：获取上传文件的输入流，通常用于自定义接收和存储文件，与`transferTo()`不会同时使用。

- `void transferTo(File dest)`：直接存储客户端提交上传的文件。

#### 1.9. 关于MultipartResolver的配置

在使用文件上传时，在Spring的配置文件中配置了`CommonsMultipartResolver`，可以配置的属性有：

- `maxUploadSize`：上传的文件的最大大小，以字节为单位，假设设置值是10M，如果一次性上传5个文件，则5个文件的大小总和不允许超过10M。

- `maxUploadSizePerFile`：上传的单个文件的最大大小，以字节为单位，，假设设置值是10M，如果一次性上传5个文件，则每个文件的大小都不允许超过10M，但是5个文件的总和可能接近50M。

- `defaultEncoding`：默认编码，用于配置客户端在上传表单中提交的其它值（例如同一个表单中的输入框中的数据等）的字符编码。

> 即便配置了`MultipartResolver`的`maxUploadSize`甚至还有`maxUploadSizePerFile`，在编写控制器中处理上传的方法时，仍应该通过`MultipartFile`的`getSize()`方法获取文件大小，并判断文件大小是否在允许的范围之内！因为，配置的`MultipartResolver`是整个上传功能的配置，在同一个项目中，可能有多个上传功能，例如上传头像、上传附件、上传商品图片等，`MultipartResolver`的配置将作用于当前项目中的所有上传功能，所以，设置值可能偏大，而在控制器中是针对某个具体的功能的配置！所以，其实可以不配置`MultipartResolver`，只需要在每个处理上传功能的控制器方法中进行判断即可！当然，如果在配置文件中已经配置了`MultipartResolver`，则可能更早的将某些不符合要求的请求拒绝掉，也是有好处的，但是，如果要配置，一定是配置一个满足所有上传功能的最大值。

