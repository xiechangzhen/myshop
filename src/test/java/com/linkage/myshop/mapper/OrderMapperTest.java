package com.linkage.myshop.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Order;
import com.linkage.myshop.entity.OrderItem;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMapperTest {
	@Autowired
	private OrderMapper orderMapper;
	
	@Test
	public void insertOrder(){
		Order order = new Order();
		order.setRecvName("liuyun");
		order.setUid(7);
		order.setOrderTime(new Date());
		orderMapper.insertOrder(order);
		System.out.println("end");
	}
	
	@Test
	public void insertOrderItem(){
		OrderItem orderItem = new OrderItem();
		orderItem.setGoodsPrice(100);
		orderItem.setOid(1);
		orderMapper.insertOrderItem(orderItem);
		System.out.println("end");
	}
}
