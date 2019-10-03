package com.linkage.myshop.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkage.myshop.entity.Address;
import com.linkage.myshop.entity.Order;
import com.linkage.myshop.entity.OrderItem;
import com.linkage.myshop.mapper.OrderMapper;
import com.linkage.myshop.service.IAddressService;
import com.linkage.myshop.service.ICartService;
import com.linkage.myshop.service.IOrderService;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.AddressNotFoundException;
import com.linkage.myshop.service.exception.CartNotFoundException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.vo.CartVO;
import com.linkage.myshop.vo.OrderVO;
@Service
public class OrderServiceImpl implements IOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private IAddressService iAddressService;
	@Autowired
	private ICartService iCartService;

	@Override
	@Transactional
	public Order addNew(Integer[] cids, Integer aid,Integer uid,String username) throws InsertException, 
	AddressNotFoundException,AccessDenisedException,CartNotFoundException{
		//得到地址
		Address address =iAddressService.getAddressByAid(aid);
		//是否有值
		if(address == null){
			throw new AddressNotFoundException("插入订单失败，地址未找到异常！");
		}
		//判断是否为本用户地址
		if(!address.getUid().equals(uid)){
			throw new AccessDenisedException("插入订单失败，该地址没有用户权限！");
		}
		//通过购物车id数组得到OrderItem数据 看购物车数据是否正确
		List<CartVO> cartVOs = iCartService.getByIds(cids);
		if(cartVOs == null || cartVOs.size() != cids.length){
			throw new CartNotFoundException("插入订单失败，购物车数据缺少！");
		}
		//准备order数据
		String recvName = address.getName();
		String recvPhone = address.getPhone();
		String recvAddress = address.getDistrict() + address.getArea();
		Integer totalPrice = 0;

		//遍历是为了得到总价
		for (CartVO cartVO : cartVOs) {
			//设置订单商品数据
			totalPrice += cartVO.getNewPrice()*cartVO.getCount();
		}
		System.out.println("totalPrice:"+totalPrice);
		Date now = new Date();
		Order order = new Order();
		//设置订单数据
		order.setUid(uid);
		//订单时间
		order.setOrderTime(now);
		//支付时间
		//order.setPayTime(now);
		order.setRecvName(recvName);
		order.setRecvPhone(recvPhone);
		order.setRecvAddress(recvAddress);
		//订单状态 0未支付
		System.out.println("order");
		order.setStatus(0);
		order.setTotalPrice(totalPrice);
		//设置4项日志
		order.setCreateUser(username);
		order.setCreateTime(now);
		order.setModifiedUser(username);
		order.setModifiedTime(now);
		//插入订单
		insertOrder(order);
		System.out.println("insertOrder");
		//插入之后得到订单id
		Integer oid = order.getOid();
		System.out.println("insertOrder:oid-->"+oid);
		//生成订单商品对象
		OrderItem orderItem = new OrderItem();
		//遍历得到的购物车数据
		for (CartVO cartVO : cartVOs) {
			//设置订单商品数据 先插入订单数据之后才有oid（订单id）
			orderItem.setOid(oid);
			orderItem.setGid(cartVO.getGid());
			orderItem.setGoodsTitle(cartVO.getTitle());
			orderItem.setGoodsImage(cartVO.getImage());
			orderItem.setGoodsPrice(cartVO.getNewPrice());
			orderItem.setGoodsNum(cartVO.getCount());
			//设置4项日志
			orderItem.setCreateUser(username);
			orderItem.setCreateTime(now);
			orderItem.setModifiedUser(username);
			orderItem.setModifiedTime(now);
			//判断是否为本用户地址
			if(!cartVO.getUid().equals(uid)){
				throw new AccessDenisedException("插入订单失败，购物车没有用户权限！");
			}
			//插入订单商品数据
			insertOrderItem(orderItem);
		}
		//待续...
		//清空已付款的购物车
		
		//减去库存，若为在规定时间内完成支付，则订单失效，加回库存
		
		return order;
	}

	@Override
	public OrderVO getByOid(Integer oid){
		return findByOid(oid);
	}
	/**
	 * 插入订单数据
	 * @param order 订单数据
	 */
	private void insertOrder(Order order){
		Integer rows = orderMapper.insertOrder(order);
		if(rows != 1){
			throw new InsertException("插入订单失败！");
		}
	}

	/**
	 * 插入订单商品数据
	 * @param orderItem 订单商品数据
	 */
	private void insertOrderItem(OrderItem orderItem){
		Integer rows = orderMapper.insertOrderItem(orderItem);
		if(rows != 1){
			throw new InsertException("插入订单异常，插入订单商品数据失败！");
		}
	}

	/**
	 * 查询订单商品数据
	 * @param oid 订单oid
	 * @return 得到匹配的订单详情，没有找到则返回null
	 */
	private OrderVO findByOid(Integer oid){
		return orderMapper.findByOid(oid);
	}


}
