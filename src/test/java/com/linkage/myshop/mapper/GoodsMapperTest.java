package com.linkage.myshop.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Goods;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsMapperTest {
	@Autowired
	private GoodsMapper goodsMapper;
	@Test
	public void findByIdTest(){
		long id = 10000002;
		Goods goods = goodsMapper.findById(id);
		System.err.println("end"+goods);
		
	}
	
	@Test
	public void findByPriority(){
		List<Goods> goods = goodsMapper.findByPriority(4);
		for (Goods goods2 : goods) {
			System.err.println("end"+goods);
		}
	}
	
	@Test
	public void findByPriority1(){
		Integer first = 128;
		Integer second = 128;
		if(first==second){
			System.out.println("first==second");
		}
		if(first.equals(second)){
			//数字小于128时两个都输出，大于时只有first.equals(second)输出
			System.out.println("first.equals(second)");
		}
	}
	
}
