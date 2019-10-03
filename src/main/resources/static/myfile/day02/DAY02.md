### 5. 用户-注册-业务层【续】

通常，在业务层中抛出异常时，应该对抛出的异常进行文字的描述，后续，将会把这些描述文字响应给客户端。

所以，应该先打开`ServiceException`，通过Eclipse的**Source**菜单中的**Generate Constructors from superclass**添加与父类相同的5个构造方法，然后，再分别打开`InsertException`和`UsernameDuplicateException`，使用相同的操作方式也添加5个构造方法。

然后，在业务方法中，抛出异常时，可以通过异常类的构造方法封装描述文字：

	@Override
	public void reg(User user) throws UsernameDuplicateException, InsertException {
		// 根据尝试注册的用户名查询数据
		User result = 
				userMapper.findByUsername(
					user.getUsername());
		// 判断查询结果是否为null
		if (result == null) {
			// 是：允许注册
			// TODO 密码加密
			// 执行注册
			Integer rows = userMapper.insert(user);
			if (rows != 1) {
				throw new InsertException(
					"注册时发生未知错误，请联系系统管理员！");
			}
		} else {
			// 否：不允许注册，抛出异常
			throw new UsernameDuplicateException(
				"尝试注册的用户名(" + user.getUsername() + ")已经被占用！");
		}
	}

业务层除了要保证数据的逻辑安全（即：数据应该根据我们设定的规则而产生或变化，例如：用户名对应的数据不存在时才可以注册该用户名），还应该保证数据在产生或发生变化时是完整的！

以此次“注册”为例，注册时的参数`User user`应该是客户端提交的，则用户可以通过客户端界面输入例如用户名、密码等信息，这些信息都会封装在这个参数中，但是，这个参数中不会包含其它不由客户端提交的数据，例如`is_delete`及4个日志数据，则在执行注册之前，需要补全这些数据！

	@Override
	public void reg(User user) throws UsernameDuplicateException, InsertException {
		// 根据尝试注册的用户名查询数据
		User result = 
				userMapper.findByUsername(
					user.getUsername());
		// 判断查询结果是否为null
		if (result == null) {
			// 是：允许注册
			// 封装is_delete
			user.setIsDelete(0);
			// 封装日志
			Date now = new Date();
			user.setCreatedUser(user.getUsername());
			user.setCreatedTime(now);
			user.setModifiedUser(user.getUsername());
			user.setModifiedTime(now);
			// TODO 密码加密
			// 执行注册
			Integer rows = userMapper.insert(user);
			if (rows != 1) {
				throw new InsertException(
					"注册时发生未知错误，请联系系统管理员！");
			}
		} else {
			// 否：不允许注册，抛出异常
			throw new UsernameDuplicateException(
				"尝试注册的用户名(" + user.getUsername() + ")已经被占用！");
		}
	}

然后，编写密码加密的方法，该方法将应用于注册和后续所有需要验证密码或加密的业务中：

	/**
	 * 将密码执行加密
	 * @param password 原密码
	 * @param salt 盐值
	 * @return 加密后的结果
	 */
	private String getMd5Password(String password, String salt) {
		// 拼接原密码与盐值
		String str = salt + password + salt;
		// 循环加密5次
		for (int i = 0; i < 5; i++) {
			str = DigestUtils.md5DigestAsHex(
					str.getBytes()).toUpperCase();
		}
		// 返回结果
		return str;
	}

接下来，在业务方法中生成随机盐值，并加密：

	@Override
	public void reg(User user) throws UsernameDuplicateException, InsertException {
		// 根据尝试注册的用户名查询数据
		User result = 
				userMapper.findByUsername(
					user.getUsername());
		// 判断查询结果是否为null
		if (result == null) {
			// 是：允许注册
			// 封装is_delete
			user.setIsDelete(0);
			// 封装日志
			Date now = new Date();
			user.setCreatedUser(user.getUsername());
			user.setCreatedTime(now);
			user.setModifiedUser(user.getUsername());
			user.setModifiedTime(now);
			// 密码加密
			// - 生成随机盐
			String salt = UUID.randomUUID().toString().toUpperCase();
			// - 基于原密码和盐值执行加密
			String md5Password = getMd5Password(
					user.getPassword(), salt);
			// - 将盐和加密结果封装到user对象中
			user.setSalt(salt);
			user.setPassword(md5Password);
			// 执行注册
			Integer rows = userMapper.insert(user);
			if (rows != 1) {
				throw new InsertException(
					"注册时发生未知错误，请联系系统管理员！");
			}
		} else {
			// 否：不允许注册，抛出异常
			throw new UsernameDuplicateException(
				"尝试注册的用户名(" + user.getUsername() + ")已经被占用！");
		}
	}

### 6. 用户-注册-控制器层

#### 6.1. 处理异常


创建`cn.tedu.store.util.ResponseResult`类，作为控制器类的响应结果类型，后续，当控制器方法返回这种类型的数据时，Jackson框架会把响应结果对象转换为JSON数据格式：

	/**
	 * 控制器向客户端响应结果的数据类型
	 * 
	 * @param <T> 如果控制器会向客户端响应某些数据，则表示响应的数据的类型
	 */
	public class ResponseResult<T> implements Serializable {
	
		private static final long serialVersionUID = 1568501256910141001L;
	
		private Integer state;
		private String message;
		private T data;
		
		// SET/GET
	}

因为控制器类将调用业务层来完成各个业务，大部分都会抛出异常，则控制器类就需要处理异常，推荐使用SpringMVC框架提供的统一处理的方式来处理异常。

为了使得每个控制器都能直接使用统一处理异常的方式，应该先创建控制器类的基类`cn.tedu.store.controller.BaseController`，并在这个类中添加处理异常的方法：

	/**
	 * 控制器类的基类
	 */
	public abstract class BaseController {
		
		/**
		 * 响应结果时用于表示操作成功
		 */
		protected static final int SUCCESS = 200;
	
		@ExceptionHandler(ServiceException.class)
		public ResponseResult<Void> handleException(ServiceException e) {
			ResponseResult<Void> rr
				= new ResponseResult<>();
			rr.setMessage(e.getMessage());
			
			if (e instanceof UsernameDuplicateException) {
				// 400-用户名冲突异常
				rr.setState(400);
			} else if (e instanceof InsertException) {
				// 500-插入数据异常
				rr.setState(500);
			}
			
			return rr;
		}
	
	}

由于该类不需要被单独创建对象，所以，类本身是不需要添加注解的！

由于该类的作用就是被其它各控制器类继承，所以，应该声明为抽象类！

无论是成功，还是某种操作失败，都应该向客户端响应操作结果的代号，推荐自行决定编号规则，例如使用200表示成功，使用4xx表示可详细描述原因的错误，使用5xx表示不便于描述原因的错误。

#### 6.x. 设计请求

在编写处理请求的方法之前，应该先规划如何处理请求：

	请求路径：/users/reg
	请求参数：User user
	请求方式：POST
	响应结果：ResponseResult<Void>

#### 6.x. 处理请求

创建`cn.tedu.store.controller.UserController`控制器类，继承自`BaseController`，添加`@RestController`和`@RequestMapping("users")`注解，并在类中声明`@Autowired private IUserService userService;`业务层对象。

然后，在控制器类中添加处理请求的方法：

	@RequestMapping("reg")
	public ResponseResult<Void> reg(User user) {
		// 执行注册
		userService.reg(user);
		// 返回成功
		return new ResponseResult<>(SUCCESS);
	}

完成后，打开浏览器，通过`http://localhost:8080/users/reg?username=handler&password=123456`进行测试，以检验控制器是否可以正常运行，且，完成后，应该将`@RequestMapping`替换为`@PostMapping`。

### 7. 用户-注册-前端页面

解压静态资源文件压缩包，将5个文件夹全部复制到项目的`src/main/resources/static`中。

注册页面是`register.html`，然后添加AJAX相关代码：

	<script type="text/javascript">
	$("#btn-reg").click(function(){
		$.ajax({
			"url":"/users/reg",
			"data":$("#form-reg").serialize(),
			"type":"post",
			"dataType":"json",
			"success":function(json) {
				if (json.state == 200) {
					alert("注册成功！");
				} else {
					alert(json.message);
				}
			}
		});
	});
	</script>

并且，在HTML代码部分，添加表单和按钮的id，为输入框设置name。

### -------------------------------------------

### 附1：密码加密

用户的密码应该执行加密后，再进行存储，以避免数据库中的数据被泄密，导致用户数据被窃取。

常见的加密算法有：AES、3DES等，并不适用于密码加密，因为这些加密算法都是可以被逆向运算的，即：如果能够得到加密过程中的所有参数，就可以根据密文（加密后的结果）逆向运算得到原文（加密前的原始数据）。

应用于密码加密的应该使用摘要算法。

摘要算法的特点：

- 使用相同的摘要算法，相同的原文，得到的摘要数据必然是相同的；

- 无论原文是什么样的数据，使用固定的摘要算法，得到的摘要数据的长度是固定的； 

- 使用固定的摘要算法，不同的原文，几乎不会得到相同的摘要数据。

所有的摘要算法都是不可被逆运算的！

在应用于密码加密时，由于密码的原文的长度是有限制的，例如限制为6~16位长度，则，在有限的原文中，找到2个不同的原文却对应相同的摘要数据，几乎就是不可能的！所以，将摘要算法应用于密码加密领域是安全的！

常见的摘要算法有SHA系列和MD系列。应用于密码加密的比较常见的就是MD5，应用于消息或下载文件的校验的可能是SHA-256或其它长度更长的消息摘要算法。

另外，也可以找到许多与“MD5破解”相关的内容！

首先，MD5破解是真实存在的！此“破解”指的是通过找到2个不同的原文，运算得到相同的摘要，从而验证该算法是不安全的，并且，运算次数并不需要2的128次方这么多。如果认为“逆运算才算破解”，本身就是对摘要算法的误解！

另外，网上也有许多网站可以实现在线解密，其实，这些网站就是记录了原文和对应的摘要，当尝试通过这些网站“解密”时，本质上是一种“反查”操作。

因为有“反查”的存在，所以，为了进一步保障用户密码安全，应该：

1. 强制要求使用强度更高的原始密码；

2. 加盐；

3. 反复加密；

4. 综合以上应用方式。

