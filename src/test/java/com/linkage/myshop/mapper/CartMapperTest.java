package com.linkage.myshop.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.vo.CartVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartMapperTest {
	@Autowired
	CartMapper cartMapper;
	
	@Test
	public void update(){
		
		cartMapper.update(3, 15, "asd", new Date());
		System.out.println("end");
	}
	
	
	@Test
	public void findByUid(){
		List<CartVO>  cartVOs = cartMapper.findByUid(7);
		for (CartVO cartVO : cartVOs) {
			System.out.println("end"+cartVO);
		}
	}
	
	
	
	
}
