### 58. 购物车-显示列表-持久层

**1. 规划SQL语句**

要显示某用户的购物车数据列表，需要执行的SQL语句大致是：

	select xx from t_cart where uid=?

但是，在`t_cart`表中存储的都是关联数据的id，例如商品数据的`gid`，即便查询到了`gid`也不可能直接用于显示界面，还应该根据`gid`查询出商品的标题、图片、单价等，为了保证查询出的结果能被直接显示到界面中，查询语句应该是：

	select cid,gid,title,image,price,t_cart.num 
	from t_cart 
	inner join t_goods 
	on t_cart.gid=t_goods.id 
	where uid=?
	order by t_cart.created_time desc
	
**2. 接口与抽象方法**

在设计抽象方法之前，需要新创建`cn.tedu.store.vo.CartVO`VO类，以用于封装查询结果：

	public class CartVO implements Serializable {
		private Integer cid;
		private Long gid;
		private String title;
		private String image;
		private Long price;
		private Integer num;
		// GET/SET
	}

然后，在`CartMapper.java`接口中添加抽象方法：

	List<CartVO> findByUid(Integer uid);

**3. 配置映射**

在`CartMapper.xml`中配置：

	<!-- 获取某用户的购物车数据列表 -->
	<!-- List<CartVO> findByUid(Integer uid) -->
	<select id="findByUid"
		resultType="cn.tedu.store.vo.CartVO">
		SELECT
			cid, gid,
			title, image,
			price, t_cart.num
		FROM
			t_cart
		INNER JOIN
			t_goods
		ON
			t_cart.gid=t_goods.id
		WHERE
			uid=#{uid}
		ORDER BY
			t_cart.created_time DESC
	</select>

然后编写并执行单元测试：

	@Test
	public void findByUid() {
		Integer uid = 11;
		List<CartVO> list = mapper.findByUid(uid);
		System.err.println("BEGIN:");
		for (CartVO item : list) {
			System.err.println(item);
		}
		System.err.println("END.");
	}

### 59. 购物车-显示列表-业务层

**1. 规划异常**

无

**2. 接口与抽象方法**

在`ICartService`中添加抽象方法：

	/**
	 * 获取某用户的购物车数据列表
	 * @param uid 用户的id
	 * @return 匹配的购物车数据列表
	 */
	List<CartVO> getByUid(Integer uid);

**3. 实现**

首先，在`CartServiceImpl`实现类中再次粘贴持久层中的方法，并将其私有化实现：

然后，重写接口中的抽象方法，调用自身的私有方法实现：

最后，在`CartServiceTestCase`中编写并执行单元测试：

### 60. 购物车-显示列表-控制器层

**1. 处理异常**

无

**2. 设计请求**

	请求路径：/carts/
	请求参数：HttpSession session
	请求类型：GET
	响应结果：ResponseResult<List<CartVO>>
	是否拦截：是，无需修改配置

**3. 处理请求**

在`CartController`中添加处理请求的方法：

	@GetMapping("/")
	public ResponseResult<List<CartVO>> getByUid(HttpSession session) {
		// 获取uid
		// 执行查询
		// 响应成功与查询结果
	}

完成后，打开浏览器，通过`http://localhost:8080/carts/`执行测试，测试之前应该先登录。

### 66. 购物车-增加商品数量-持久层

**1. 规划SQL语句**

增加商品数量的SQL语句应该是：

	UPDATE xx SET num=?,modified_user=?,modified_time=? WHERE cid=?

该功能已经完成！无需再次开发。

在增加数量之前，还应该检查该数据是否存在！

	SELECT xx FROM t_cart WHERE cid=?

并检查数据归属是否正确，则查询时需要获取`uid`字段的值。

后续更新商品数量时，还需要知道原数量是多少，则查询时需要获取`num`字段的值。

**2. 接口与抽象方法**

	Cart findByCid(Integer cid);

**3. 配置映射**

	<!-- 根据购物车数据的id查询购物车数据 -->
	<!-- Cart findByCid(Integer cid) -->
	<select id="findByCid"
		resultType="cn.tedu.store.entity.Cart">
		SELECT 
			uid,num
		FROM 
			t_cart
		WHERE 
			cid=#{cid}
	</select>

### 67. 购物车-增加商品数量-业务层

**1. 规划异常**

当更新商品数量时，可能出现`UpdateException`。

当检查购物车数据是否存在时，可能出现`CartNotFoundException`。

当检查购物车数据归属时，可能出现`AccessDeniedException`。

以上3种异常中，需要创建的是`CartNotFoundException`。

**2. 接口与抽象方法**

	Integer addNum(Integer cid, Integer uid, String username) throws CartNotFoundException, AccessDeniedException, UpdateException;

**3. 实现**

将持久层的`Cart findByCid(Integer cid);`方法复制到业务层实现类中，并私有化实现。

在业务层实现类中重写抽象方法：

	public Integer addNum(Integer cid, Integer uid, String username) throws CartNotFoundException, AccessDeniedException, UpdateException {
		// 根据参数cid查询数据
		// 检查查询结果是否为null
		// 是：CartNotFoundException

		// 检查参数uid与查询结果中的uid是否不同
		// 是：AccessDeniedException

		// 取出查询结果中的商品数量，增加1，得到新的数量
		// 更新商品数量：updateNum(Integer cid, Integer num, 
		String modifiedUser, Date modifiedTime)
		// 返回新的数量
	}

具体实现为：

	@Override
	public Integer addNum(Integer cid, Integer uid, String username)
			throws CartNotFoundException, AccessDeniedException, UpdateException {
		// 根据参数cid查询数据
		Cart result = findByCid(cid);
		// 检查查询结果是否为null
		if (result == null) {
			// 是：CartNotFoundException
			throw new CartNotFoundException(
				"增加商品数量失败！尝试访问的购物车数据不存在！");
		}

		// 检查参数uid与查询结果中的uid是否不同
		if (uid != result.getUid()) {
			// 是：AccessDeniedException
			throw new AccessDeniedExcption(
				"增加商品数量失败！尝试访问的购物车数据归属错误！");
		}

		// 取出查询结果中的商品数量，增加1，得到新的数量
		Integer newNum = result.getNum() + 1;
		// 更新商品数量：updateNum(Integer cid, Integer num, String modifiedUser, Date modifiedTime)
		updateNum(cid, newNum, username, new Date());
		// 返回新的数量
		return newNum;
	}

单元测试：

	@Test
	public void addNum() {
		try {
			Integer cid = 3;
			Integer uid = 11;
			String username = "小刘同学";
			Integer newNum = service.addNum(cid, uid, username);
			System.err.println("OK. new num=" + newNum);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 68. 购物车-增加商品数量-控制器层

**1. 处理异常**

处理`CartNotFoundException`。

**2. 设计请求**

	请求路径：/carts/{cid}/add_num
	请求参数：@PathVariable("cid") Integer cid, HttpSession session
	请求类型：POST
	响应数据：ResponseResult<Integer>

**3. 处理请求**

	@RequestMapping("{cid}/add_num")
	public ResponseResult<Integer> addNum(@PathVariable("cid") Integer cid, HttpSession session) {
		// 获取uid和username
		// 调用业务层对象执行增加并获取结果：addNum(Integer cid, Integer uid, String username)
		// 返回成功和结果(新的数量)
	}

完成后，通过`http://localhost:8080/carts/5/add_num`执行测试。