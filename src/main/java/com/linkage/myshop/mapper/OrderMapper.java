package com.linkage.myshop.mapper;


import com.linkage.myshop.entity.Order;
import com.linkage.myshop.entity.OrderItem;
import com.linkage.myshop.vo.OrderVO;

/**
 * 订单持久层接口
 * @author Administrator
 *
 */
public interface OrderMapper {

	/**
	 * 插入订单数据
	 * @param order 订单数据
	 * @return 成功影响的行数
	 */
	Integer insertOrder(Order order);

	/**
	 * 插入订单商品数据
	 * @param orderItem 订单商品数据
	 * @return 成功影响的行数
	 */
	Integer insertOrderItem(OrderItem orderItem);
	
	/**
	 * 查询订单商品数据
	 * @param oid 订单oid
	 * @return 得到匹配的订单详情，没有找到则返回null
	 */
	OrderVO findByOid(Integer oid);
}
