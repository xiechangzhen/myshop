package com.linkage.myshop.service;

import java.util.List;


import com.linkage.myshop.entity.Goods;
/**
 * 商品的业务层接口
 * @author Administrator
 *
 */
public interface IGoodsService {

	/**
	 * 根据商品分类id获取商品的数据列表
	 * @param categoryId 商品分类id
	 * @return 商品的的数据列表
	 */
	List<Goods> getByCategoryId(Long categoryId,Integer offSet, Integer count);

	/**
	 * 根据商品id获取商品的详情
	 * @param id 商品分类id
	 * @return 商品的的数据
	 */
	Goods getById(Long id);
	
	
	/**
	 * 根据优先级获取商品数据的列表
	 * @param count 要显示前几条的数据量
	 * @return 优先级最高的几个商品的数据的列表
	 */
	List<Goods> getByPriority(Integer count);
}
