package com.linkage.myshop.service;

import java.util.List;

import com.linkage.myshop.entity.Cart;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.CartNotFoundException;
import com.linkage.myshop.service.exception.CountLimitException;
import com.linkage.myshop.service.exception.DeleteException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.UpdateException;
import com.linkage.myshop.vo.CartVO;
/**
 * 购物车的业务层接口
 * @author Administrator
 *
 */
public interface ICartService {	/**
	 * 向购物车中添加一条新的数据
	 * @param cart 购物车数据
	 * @param uid 用户uid
	 * @param username 用户名
	 * @throws InsertException 新增购物车数据异常
	 * @throws UpdateException 更新购物车数据异常
	 */
	void insert(Cart cart,Integer uid,String username) throws InsertException , UpdateException;
	
	/**
	 * 根据购物车cid数组删除购物车的数据
	 * @param c ids 商品cid的数组
	 * @throws DeleteException 删除购物车数据异常
	 */
	void deleteCart(Integer uid,Integer[] cids) throws DeleteException,CartNotFoundException, AccessDenisedException;
	
	/**
	 * 查询用户的购物车列表
	 * @param uid 用户uid
	 * @return 得到匹配的购物车列表数据
	 */
	List<CartVO> getByUid(Integer uid);
	
	/**
	 * 根据购物车主键cids数组得到购物车的数据
	 * @param ids 购物车主键cids的数组
	 * @return 得到购物车的数据
	 */
	List<CartVO> getByIds(Integer[] cids);

	/**
	 * 修改购物车商品数量
	 * @param uid 用户id
	 * @param id 购物车cid
	 * @param username 用户名
	 * @throws CartNotFoundException 该购物车id数据找不到异常
	 * @throws AccessDenisedException 该购物车id数据不属于本用户，访问权限异常
	 * @throws UpdateException 更新异常购物车数量异常
	 */
	void updateCount(Integer uid,Integer cid,String username,Boolean flag) throws CartNotFoundException,AccessDenisedException,UpdateException,CountLimitException;
}
