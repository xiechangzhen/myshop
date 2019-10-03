package com.linkage.myshop.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Goods;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceImplTest {
	@Autowired
	private IGoodsService IGoodsService; 
	
	@Test
	public void getByCategoryId(){
		long categoryId = 238;
		List<Goods> List
		= IGoodsService.getByCategoryId(categoryId, 1, 20);
		for (Goods goods : List) {
			System.err.println("end"+goods);
		}
		System.err.println("end0");
	}
	
	@Test
	public void getById(){
		long id = 10000002;
		Goods goods = IGoodsService.getById(id);
		System.err.println("end"+goods);
		System.err.println("end0");
	}
	
}
