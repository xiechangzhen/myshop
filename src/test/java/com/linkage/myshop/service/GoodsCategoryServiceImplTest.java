package com.linkage.myshop.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.GoodsCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsCategoryServiceImplTest {
	
	@Autowired
	private IGoodsCategoryService iGoodsCategoryService;
	
	@Test
	public void  getByParentIdTest(){
		long parentId = 7;
		List<GoodsCategory> list = iGoodsCategoryService.getByParentId(parentId);
		System.out.println("end");
	}
}
