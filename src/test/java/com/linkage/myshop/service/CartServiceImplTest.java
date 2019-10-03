package com.linkage.myshop.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Cart;
import com.linkage.myshop.service.exception.ServiceException;
import com.linkage.myshop.vo.CartVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceImplTest {
	@Autowired
	private ICartService iCartService;

	
	@Test
	public void insertTest(){
		try {
			Cart cart = new Cart();
			cart.setGid(10000004L);
			cart.setPrice(1000L);
			cart.setCount(10);
			Integer uid=4;
			String username= "ab";
			
			System.err.println("begin");
			iCartService.insert(cart, uid, username);
			System.err.println("end0");
		} catch (ServiceException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
		System.err.println("end");
	}
	
	@Test
	public void deleteCart(){
		Integer uid = 7;
		Integer[] ids = {2,5};
		iCartService.deleteCart(uid, ids);
		System.out.println("end:");
		
	}
	
	@Test
	public void getByUid(){
		List<CartVO> carts = iCartService.getByUid(7);
		for (CartVO cartVO : carts) {
			System.out.println("end:"+cartVO);
		}
	}
	
	@Test
	public void getByIdsTest(){
		Integer [] ids = {1,2,5};
		List<CartVO> cartVOs = iCartService.getByIds(ids);
		for (CartVO cartVO : cartVOs) {
			System.out.println("end:"+cartVO);
		}
	}
	
	@Test
	public void updateCountTest(){
		try {
			Integer uid = 7;
			Integer id = 3;
			String username = "nihao";
			Boolean flag = false ;
			iCartService.updateCount(uid, id, username, flag);
			System.err.println("end");
			
		} catch (ServiceException e) {
			System.err.println(e.getClass());
			System.err.println(e.getMessage());
		}
	}
	
}
