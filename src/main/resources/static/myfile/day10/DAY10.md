### 40. 收货地址-删除-业务层

**1. 规划异常**

此次主要执行删除操作，则可能抛出`DeleteException`。

在执行操作之前，还应该检查数据是否存在和数据归属问题，则可能抛出`AddressNotFoundException`和`AccessDeniedException`。

同时，由于可能删除了默认收货地址，就可能需要再把另一条设置为默认，则可能抛出`UpdateException`。

以上4种异常中，需要新创建`DeleteException`。

**2. 抽象方法**

	void delete(Integer aid, Integer uid, String username) throws 4种异常;

**3. 实现功能**

首先，在`AddressServiceImpl`中添加持久层中的2个新方法，将它们私有化实现，其中，删除的方法需要获取返回值，并判断返回值是否是预期值，如果不是，则抛出异常。

然后，重写接口中的抽象方法：

	public void delete(Integer aid, Integer uid, String username) throws 4种异常 {
		// 根据参数aid查询即将删除的数据：findByAid(aid)
		// 判断查询结果是否为null
		// 是：AddressNotFoundException

		// 判断查询结果中的uid与参数uid是否不同
		// 是：AccessDeniedException

		// 执行删除：deleteByAid(aid)

		// 判断查询结果的isDefault是否为1：需要在findByAid的SQL查询中补充查询is_default字段
		// 是：表示删除了默认收货地址，则统计收货地址数量：countByUid(uid)
		// 判断数量是否大于0
		// 找出最后修改的收货地址：findLastModified(uid)
		// 获取该收货地址数据的aid：Integer lastModifiedAid = xx.getAid()
		// 创建当前时间对象
		// 将该收货地址设置为默认：updateDefault(lastModifiedAid, username, now)
	}

具体实现为：

	@Override
	@Transactional
	public void delete(Integer aid, Integer uid, String username)
			throws AddressNotFoundException, AccessDeniedExcption, UpdateException, DeleteException {
		// 根据参数aid查询即将删除的数据：findByAid(aid)
		Address result = findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：AddressNotFoundException
			throw new AddressNotFoundException(
				"删除收货地址失败！尝试访问的数据不存在！");
		}
		
		// 判断查询结果中的uid和参数uid是否不一致（需要检查持久层的查询功能是否查询了uid）
		if (result.getUid() != uid) {
			// 是：AccessDeniedException
			throw new AccessDeniedExcption(
				"删除收货地址失败！访问被拒绝！");
		}

		// 执行删除：deleteByAid(aid)
		deleteByAid(aid);

		// 判断查询结果的isDefault是否为0：删除的不是默认收货地址，后续不需要补充操作，直接结束
		if (result.getIsDefault() == 0) {
			return;
		}
		
		// 表示删除了默认收货地址，则统计收货地址数量：countByUid(uid)
		Integer count = countByUid(uid);
		// 判断数量是否为0，是：刚才删除的是最后一条数据，后面没有数据了，则不需要补充操作
		if (count == 0) {
			return;
		}
		
		// 找出最后修改的收货地址：findLastModified(uid)
		Address lastModified = findLastModified(uid);
		// 获取该收货地址数据的aid：Integer lastModifiedAid = xx.getAid()
		Integer lastModifiedAid = lastModified.getAid();
		// 创建当前时间对象
		Date now = new Date();
		// 将该收货地址设置为默认：updateDefault(lastModifiedAid, username, now)
		updateDefault(lastModifiedAid, username, now);
	}

测试：

	@Test
	public void delete() {
		try {
			Integer aid = 22;
			Integer uid = 15;
			String username = "哈哈哈";
			service.delete(aid, uid, username);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}

### 41. 收货地址-删除-控制器层

**1. 处理异常**

此次业务层抛了新的异常：`DeleteException`。

**2. 设计请求**

	请求路径：/addresses/{aid}/delete
	请求参数：HttpSession session
	请求类型：POST
	响应数据：ResponseResult<Void>

**3. 处理请求**

	@RequestMapping("/{aid}/delete")
	public ResponseResult<Void> delete(
		@PathVariable("aid") Integer aid,
		HttpSession session) {
		// 获取uid和username
		// 执行
		// 返回
	}

### 42. 商品-数据表

从FTP下载`t_goods.zip`解压后导入**商品分类表**和**商品表**，及表中数据。

注：如果导入`t_goods.zip`中的脚本出现乱码，则下载`t_goods2.zip`再次导入。

### 43. 商品-实体类

创建`cn.tedu.store.entity.Goods`实体类，表示商品数据。

	/**
	 * 商品数据的实体类
	 */
	public class Goods extends BaseEntity {
	
		private static final long serialVersionUID = 5960164494648879998L;
	
		private Long id;
		private Long categoryId;
		private String itemType;
		private String title;
		private String sellPoint;
		private Long price;
		private Integer num;
		private String barcode;
		private String image;
		private Integer status;
		private Integer priority;

		// SET/GET
	}

注：暂不需要创建**商品分类**的实体类。

### 44. 主页热销排行-持久层

**1. SQL**

	SELECT id,title,price,image 
	FROM t_goods 
	WHERE status=1 
	ORDER BY priority DESC 
	LIMIT 0,4

**2. 接口与抽象方法**

创建`cn.tedu.store.mapper.GoodsMapper`接口，并添加抽象方法：

	List<Goods> findHotList();

**3. 配置映射**

复制得到`src/main/resources/mapper/GoodsMapper.xml`文件，删除原有子级节点，修改根节点的`namespace`属性，并配置以上抽象方法的映射：

	<mapper namespace="cn.tedu.store.mapper.GoodsMapper">

		<!-- 获取热销的前4项商品的数据列表 -->
		<!-- List<Goods> findHotList() -->
		<select id="findHotList"
			resultType="cn.tedu.store.entity.Goods">
			SELECT 
				id,title,
				price,image 
			FROM 
				t_goods 
			WHERE 
				status=1 
			ORDER BY 
				priority DESC 
			LIMIT 
				0,4
		</select>
		
	</mapper>

编写并执行单元测试：

	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class GoodsMapperTestCase {
	
		@Autowired
		GoodsMapper mapper;
		
		@Test
		public void findByUid() {
			List<Goods> list = mapper.findHotList();
			System.err.println("BEGIN:");
			for (Goods item : list) {
				System.err.println(item);
			}
			System.err.println("END.");
		}
		
	}

### 45. 主页热销排行-业务层

**1. 异常**

无

**2. 接口与抽象方法**

创建`cn.tedu.store.service.IGoodsService`接口，并添加抽象方法：

	/**
	 * 获取热销的前4项商品的数据列表
	 * @return 热销的前4项商品的数据列表
	 */
	List<Goods> getHotList();

**3. 实现类**

创建`cn.tedu.store.service.impl.GoodsServiceImpl`实现类，在类之前添加`@Service`注解，在类中声明`@Autowired private GoodsMapper goodsMapper;`持久层对象。

复制持久层接口中的抽象方法到实现类，使用私有权限，并直接调用持久层对象实现方法。

重写接口中的抽象方法，通过调用自身的私有方法来实现。

### 46. 主页热销排行-控制器层

**1. 异常**

无

**2. 设计请求**

	请求路径：/goods/hot
	请求参数：无
	请求类型：GET
	响应数据：ResponseResult<List<Goods>>
	是否拦截：否，需要在拦截器的配置中添加白名单

**3. 处理请求**

首先，打开拦截器的配置类，在其中添加`/goods/**`到白名单中。

然后，创建`cn.tedu.store.controller.GoodsController`控制器类，继承自`BaseController`，在类之前添加`@RestController`和`@RequestMapping("goods")`注解，并在类中声明`@Autowired private IGoodsService goodsService`业务层对象。

然后，添加处理请求的方法：

	@GetMapping("hot")
	public ResponseResult<List<Goods>> getHostList() {
		// 调用业务层对象查询数据，并获取返回的结果
		// 返回“成功”和以上查询到的结果
	}

完成后，通过`http://localhost:8080/goods/hot`进行测试。

### 47. 主页热销排行-前端界面

首先，打开拦截器的配置类，在其中添加`/web/index.html`到白名单中。
