package com.linkage.myshop.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Goods;
import com.linkage.myshop.entity.GoodsCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsCategoryMapperTest {
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Test
	public void findByIdTest(){
		long parentId = 7;
		 List<GoodsCategory> goodsCategory = goodsCategoryMapper.findByParentId(parentId);
		 for (GoodsCategory goodsCategory2 : goodsCategory) {
			 System.err.println("end"+goodsCategory2);
		}
		 
	}
}
