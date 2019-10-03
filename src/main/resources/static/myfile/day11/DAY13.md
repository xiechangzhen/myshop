### 显示确认订单页-持久层

**1. 规划SQL语句**

在“显示确认订单页”，需要显示当前登录的用户的收货地址列表，还需要显示所勾选的购物车数据列表。

在`AddressMapper`中已经完成显示当前登录的用户的收货地址列表的功能。

目前，尚未开发“显示所勾选的购物车数据列表”功能，则需要添加，该功能对应的SQL语句大致是：

	select cid,gid,title,image,price,t_cart.num 
	from t_cart 
	inner join t_goods 
	on t_cart.gid=t_goods.id 
	where cid in (?,?,?)
	order by t_cart.created_time desc

即：相对于此前已经完成的“显示当前用户的购物车数据列表”，此次只是查询时的WHERE子句不同，改为`where cid in (?,?,?)`。

**2. 接口与抽象方法**

由于以上功能应该是处理购物车数据的相关功能，所以，在`CartMapper.java`接口中添加抽象方法：

	List<CartVO> findByCids(Integer[] cids);

**3. 配置映射**

在`CartMapper.xml`中添加节点配置映射：

	<select id="findByCids" resultType="xx.xx.xx.xx.CartVO">
		select 
			cid,gid,title,image,price,t_cart.num 
		from 
			t_cart 
		inner join 
			t_goods 
		on 
			t_cart.gid=t_goods.id 
		where 
			cid in (
			<foreach collection="cids"
				item="cid" separator=",">
				#{cid}
			</foreach>
			)
		order by 
			t_cart.created_time desc
	</select>

### 显示确认订单页-业务层

与其它查询数据的业务层开发流程和套路都相同。

### 显示确认订单页-控制器层

HTML页面使用checkbox选项框加上name和value属性 然后结算按钮是submit类型，method为get方式提交form表单到orderConfirm.html
提交到后台控制器也只要直接截取地址？后面的一串字符，全部提交过去控制器，控制器会自动把相同的参数为数组的形式接受。
此次“获取用户勾选的购物车数据列表”的URL应该是`http://localhost:8080/carts/get_by_cids?cids=3&cids=4&cids=5`。
	
	cids=3&cids=4&cids=5`用下面的获取
	var cart_id = window.location.search.substr(1); 

处理请求的方法：

	@GetMapping("get_by_cids")
	public ResponseResult<List<CartVO>> getByCids(
		Integer[] cids) {
		// 直接调用业务层对象执行查询，并获取结果
		// 返回成功与查询结果
	}

### 创建订单-创建数据表

创建订单表：

	CREATE TABLE t_order (
		oid INT AUTO_INCREMENT COMMENT '订单id',
		uid INT NOT NULL COMMENT '归属用户',
		recv_name VARCHAR(20) COMMENT '收货人姓名',
		recv_phone VARCHAR(20) COMMENT '收货人电话',
		recv_address VARCHAR(100) COMMENT '收货人详细地址',
		total_price BIGINT COMMENT '总价',
		status INT COMMENT '状态：0-未支付，1-已支付，2-已取消',
		order_time DATETIME COMMENT '下单时间',
		pay_time DATETIME COMMENT '支付时间',
		created_user VARCHAR(20) COMMENT '创建者',
		created_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '最后修改者',
		modified_time DATETIME COMMENT '最后修改时间',
		PRIMARY KEY(oid)
	) DEFAULT CHARSET=UTF8;

创建订单商品表：

	CREATE TABLE t_order_item (
		id INT AUTO_INCREMENT COMMENT '自动递增的数据id',
		oid INT COMMENT '归属的订单的id',
		gid BIGINT COMMENT '商品id',
		goods_title VARCHAR(100) COMMENT '商品标题',
		goods_image VARCHAR(500) COMMENT '商品图片',
		goods_price BIGINT COMMENT '商品价格',
		goods_num INT COMMENT '商品数量',
		created_user VARCHAR(20) COMMENT '创建者',
		created_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '最后修改者',
		modified_time DATETIME COMMENT '最后修改时间',
		PRIMARY KEY(id)
	) DEFAULT CHARSET=UTF8;

### 创建订单-实体类

略

### 创建订单-持久层

创建`cn.tedu.store.mapper.OrderMapper`接口文件，并添加抽象方法：

	Integer insertOrder(Order order);

	Integer insertOrderItem(OrderItem orderItem);

复制得到`OrderMapper.xml`，根节点的`namespace`属性值修改为以上接口，然后，配置以上2个抽象方法对应的映射：

	<mapper namespace="cn.tedu.store.mapper.OrderMapper">
		
		<!-- 插入订单数据 -->
		<!-- Integer insertOrder(Order order) -->
		<insert id="insertOrder"
			useGeneratedKeys="true"
			keyProperty="oid">
			INSERT INTO t_order (
				uid, recv_name,
				recv_phone, recv_address,
				total_price, status,
				order_time, pay_time,
				created_user, created_time,
				modified_user, modified_time
			) VALUES (
				#{uid}, #{recvName},
				#{recvPhone}, #{recvAddress},
				#{totalPrice}, #{status},
				#{orderTime}, #{payTime},
				#{createdUser}, #{createdTime},
				#{modifiedUser}, #{modifiedTime}
			)
		</insert>
		
		<!-- 插入订单商品数据 -->
		<!-- Integer insertOrderItem(OrderItem orderItem) -->
		<insert id="insertOrderItem"
			useGeneratedKeys="true"
			keyProperty="id">
			INSERT INTO t_order_item (
				oid, gid,
				goods_title, goods_image,
				goods_price, goods_num,
				created_user, created_time,
				modified_user, modified_time
			) VALUES (
				#{oid}, #{gid},
				#{goodsTitle}, #{goodsImage},
				#{goodsPrice}, #{goodsNum},
				#{createdUser}, #{createdTime},
				#{modifiedUser}, #{modifiedTime}
			)
		</insert>
		
	</mapper>

测试：

	@Test
	public void insertOrder() {
		Order order = new Order();
		order.setUid(9527);
		Integer rows = mapper.insertOrder(order);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void insertOrderItem() {
		OrderItem orderItem = new OrderItem();
		orderItem.setOid(1);
		Integer rows = mapper.insertOrderItem(orderItem);
		System.err.println("rows=" + rows);
	}

### 创建订单-业务层

创建`cn.tedu.store.service.IOrderService`接口，并添加抽象方法：

	Order create(Integer aid, Integer[] cids, Integer uid, String username) throws InsertException;

创建`cn.tedu.store.service.OrderServiceImpl`实现类，按照常规流程完成创建过程。

在实现类中，私有化实现持久层的2个方法。

在实现抽象方法之前，还需要在`IAddressService`中添加新的抽象方法并在`AddressServiceImpl`中实现：

	Address getByAid(Integer aid);

由于业务层的查询功能都是基于持久层查询功能的！在`AddressMapper`中已经存在`Address findByAid(Integer aid)`方法，则无需开发新的方法，只需要检查配置的SQL语句是否查询了必要的字段即可！目前查询的字段不足以满足需求，所以，需要补充查询`phone`、`district`、`address`字段。

然后，实现抽象方法：

	@Autowired
	private IAddressService addressService;

	@Autowired
	private ICartService cartService;

	@Transactional
	public Order create(Integer aid, Integer[] cids, Integer uid, String username) throws InsertException {
		// 创建当前时间对象：Date now = new Date();

		// 定义总价变量totalPrice = 0
		// 基于参数cids查询得到List<CartVO>：cartService.getByCids(cids)
		// 遍历集合
		// -- 遍历过程中基于商品单价和数量，得到该样商品的总价，并累加到totalPrice

		// 创建对象：Order order = new Order();
		// 封装order属性：基于参数uid
		// 封装order属性：基于aid查询得到3个收货相关数据：addressService.getByAid(aid)
		// 封装order属性：total_price
		// 封装order属性：status -> 0
		// 封装order属性：order_time -> now
		// 封装order属性：基于参数username和now封装4项日志
		// 插入订单数据：insertOrder(order);

		// 遍历List<CartVO>
		// -- 创建订单商品数据：OrderItem orderItem = new OrderItem();
		// -- 封装orderItem属性：oid -> order.getOid();
		// -- 封装orderItem属性：基于遍历到的对象，封装商品相关数据
		// -- 封装orderItem属性：基于参数username和now封装4项日志
		// -- 循环插入订单商品数据：insertOrderItem(orderItem)
	}





IoC：目标
DI：手段

通过DI实现了IoC

解耦

AOP：面向切面

	注册		UserController --> IUserService.reg() --> UserMapper

	登录		UserController --> IUserService.login() --> UserMapper


