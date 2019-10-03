package com.linkage.myshop.service;

import com.linkage.myshop.entity.Order;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.AddressNotFoundException;
import com.linkage.myshop.service.exception.CartNotFoundException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.vo.OrderVO;

/**
 * 订单业务层接口
 * @author Administrator
 *
 */
public interface IOrderService {
	
	/**
	 * 创建订单
	 * @param cids 购物车的cid数组 
	 * @param aid 收货地址的id
	 * @param uid 用户的id
	 * @param username 用户名
	 * @throws InsertException 插入异常
	 * @throws AddressNotFoundException 地址未找到异常
	 * @throws AccessDenisedException 地址未找到异常
	 * @throws CartNotFoundException 购物车找不到异常
	 */
	Order addNew(Integer[] cids,Integer aid,Integer uid,String username) throws InsertException, 
	AddressNotFoundException,AccessDenisedException,CartNotFoundException;

	/**
	 * 查询订单商品数据
	 * @param oid 订单oid
	 * @return 得到匹配的订单详情，没有找到则返回null
	 */
	OrderVO getByOid(Integer oid);
}
