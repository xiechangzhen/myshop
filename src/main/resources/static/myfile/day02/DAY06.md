		//TODO 判断文件是否为空 TODO还未做完(右侧有标志)
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

首先，先确保文件是可以上传的：

	@PostMapping("change_avatar")
	public ResponseResult<String> changeAvatar(
		HttpServletRequest request, 
		@RequestParam("avatar") MultipartFile avatar, 
		HttpSession session) {
		// 确定保存到哪个文件夹
		String parentPath = request.getServletContext().getRealPath(UPLOAD_DIR);
		File parent = new File(parentPath);
		if (!parent.exists()) {
			parent.mkdirs();
		}
		
		// 确定保存的文件名
		String originalFilename = avatar.getOriginalFilename();
		String suffix = "";
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex != -1) {
			suffix = originalFilename.substring(beginIndex);
		}
		String child = UUID.randomUUID().toString() + suffix;
		
		// 确定保存到哪个文件
		File dest = new File(parent, child);
		// 保存头像文件
		avatar.transferTo(dest);
		
		// 执行修改头像
		return null;
	}

然后，确保数据能写入到数据表中：

	@PostMapping("change_avatar")
	public ResponseResult<String> changeAvatar(
		HttpServletRequest request, 
		@RequestParam("avatar") MultipartFile avatar, 
		HttpSession session) {
		// 确定保存到哪个文件夹
		String parentPath = request
				.getServletContext()
					.getRealPath(UPLOAD_DIR);
		File parent = new File(parentPath);
		if (!parent.exists()) {
			parent.mkdirs();
		}
		
		// 确定保存的文件名
		String originalFilename = avatar.getOriginalFilename();
		String suffix = "";
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex != -1) {
			suffix = originalFilename.substring(beginIndex);
		}
		String child = UUID.randomUUID().toString() + suffix;
		
		// 确定保存到哪个文件
		File dest = new File(parent, child);
		// 保存头像文件
		try {
			avatar.transferTo(dest);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 执行修改头像
		Integer uid = getUidFromSession(session);
		String username = session.getAttribute("username").toString();
		String avatarPath = UPLOAD_DIR + "/" + child;
		userService.changeAvatar(uid, avatarPath, username);
		
		// 返回
		ResponseResult<String> rr = new ResponseResult<>();
		rr.setState(SUCCESS);
		rr.setData(avatarPath);
		return rr;
	}

### 23. 用户-上传头像-前端页面

当完成控制器端的基本上传功能后，就可以直接开始编写前端页面，以尽早的测试较少的代码，验证功能是否正常，当功能已经正常后，再补全控制器中剩余的相关检查等代码。

此次使用的前端页面是`upload.html`，该页面需要实现的功能与此前其它页面相同，也都是需要提交AJAX请求到服务器端的控制器，所以，可以从前序的其它页面中复制相关代码再进行调整。

相对于普通的表单数据提交，此次涉及文件上传时，区别在于：

1. 不再使用`表单对象.serialize()`，而是需要通过`new FormData(表单对象)`的方式获取需要提交的数据；

2. 调用`$.ajax()`函数时，需要多配置2个属性：`"contentType":false`和`"processData":false`

具体的代码实现为：

	<script type="text/javascript">
	$("#btn-change-avatar").click(function(){
		$.ajax({
			"url":"/users/change_avatar",
			"data":new FormData($("#form-change-avatar")[0]),
			"contentType":false,
			"processData":false,
			"type":"post",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("上传头像成功！");
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

剩下的，就是修改标签的id和表单控件的name值。

完成后，可以测试功能，与传统的SpringMVC项目不同，由于SpringBoot内置Tomcat，如果上传成功，在Eclipse中是可以看到上传的文件的。

接下来，应该在上传成功后显示所上传的头像图片，需要先为显示头像的`<img>`标签分配id值，然后，当上传成功后：

	$("#img-avatar").attr("src", json.data);

目前，上传成功后，可以显示所上传的图片，但是，刷新或重新打开页面时，仍显示默认的头像图片，并不是所上传的图片！要解决这个问题：

1. 当登录成功后，服务器端就将头像路径响应给客户端，并且，客户端应该将头像路径存储到Cookie中；

2. 当打开“修改头像”页面时，检查Cookie中是否存在头像路径，如果存在，则显示；

3. 当修改头像成功后，需要更新Cookie中存储的头像路径。

在代码中的表现为：

1. 服务器端用于执行“登录”的查询中，是否查询了`avatar`字段，如果没有，需要补充查询该字段；

2. 在`login.html`中，客户端登录成功时，需要将头像保存到Cookie中：

	$.cookie("username", json.data.username, {expire: 7});
	$.cookie("avatar", json.data.avatar, {expire: 7});

3. 在`upload.html`中，页面加载完成时就检查头像，如果有，则显示：

	$(document).ready(function() {
		if ($.cookie("avatar") != null) {
			$("#img-avatar").attr("src", $.cookie("avatar"));
		}
	});

4. 在`upload.html`中，上传成功后需要更新Cookie中存储的头像路径：

	$.cookie("avatar", json.data, {expire: 7});

### 24. 收货地址-分析

关于收货地址相关的功能有：显示列表，增加收货地址，修改，删除，设为默认。

这些功能的开发顺序应该是：增加 > 显示列表 > 设为默认 > 删除 > 修改。

由于现在是第1次接触这类数据，则还应该在此之前完成创建数据表、创建实体类这2个步骤。

### 25. 收货地址-创建数据表

创建`t_address`数据表：

	CREATE TABLE t_address (
		aid INT AUTO_INCREMENT COMMENT '收货地址id',
		uid INT COMMENT '归属用户的id',
		name VARCHAR(20) COMMENT '收货人',
		province CHAR(6) COMMENT '省',
		city CHAR(6) COMMENT '市',
		area CHAR(6) COMMENT '区',
		district VARCHAR(50) COMMENT '省市区的汉字名称',
		zip CHAR(6) COMMENT '邮编',
		address VARCHAR(50) COMMENT '详细地址',
		tel VARCHAR(20) COMMENT '固话',
		phone VARCHAR(20) COMMENT '手机',
		tag VARCHAR(20) COMMENT '地址类型',
		is_default INT COMMENT '是否默认：0-非默认，1-默认',
		create_user VARCHAR(20) COMMENT '创建者',
		create_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '最后修改者',
		modified_time DATETIME COMMENT '最后修改时间',
		PRIMARY KEY (aid)
	) DEFAULT CHARSET=UTF8;

### 26. 收货地址-创建实体类

创建`cn.tedu.store.entity.Address`实体类：

### 27. 收货地址-增加-持久层

**1. 规划SQL语句**

向收货地址数据表中增加新数据的SQL语句大致是：

	INSERT INTO t_address (除了aid以外的字段列表) VALUES (除了aid以外的属性值列表)

在执行增加之前，还应该确定`is_default`的值是多少！可以设置规则“增加的第1条是默认的，后续增加的都不是默认的”。则需要“判断当前即将增加的收货地址是不是第1条”，可以通过“根据uid查询数据的数量”来实现这个判断：

	SELECT COUNT(aid) FROM t_address WHERE uid=?

**2. 接口与抽象方法**

创建`cn.tedu.store.mapper.AddressMapper`持久层接口，并添加以上2个功能对应的抽象方法：

	Integer insert(Address address);

	Integer countByUid(Integer uid);

**3. 配置XML映射**

复制得到`AddressMapper.xml`文件，删除原文件中各子级节点的配置，修改根节点的`namespace`属性的值，然后，添加2个子节点以配置以上2个抽象方法的映射：

然后，在`src/test/java`下创建`cn.tedu.store.mapper.AddressMapperTestCase`测试类，在类之前添加2个注解，然后编写2个测试方法，以测试持久层的2个功能：

### 28. 收货地址-增加-业务层

**1. 规划异常**

**2. 接口与抽象方法**

**3. 实现抽象方法**

### 29. 收货地址-增加-控制器层

**1. 处理异常**

**2. 设计请求**

	请求路径：
	请求参数：
	请求类型：
	响应数据：

**3. 处理请求**

### 30. 收货地址-增加-前端界面















### 附1：在HTML页面中使用Cookie

在jQuery中的`$.cookie()`函数是用于操作Cookie的函数。

当需要向Cookie中存入数据时：

	$.cookie(数据名, 数据值, {expire: 有效期为多少天});

同样是`$.cookie()`函数，当只有1个参数时，表示从Cookie中获取数据：

	$.cookie(数据名);

**注意：使用以上函数需要引用jquery.cookie.js文件！**