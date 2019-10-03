package com.linkage.myshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkage.myshop.entity.Goods;

/**
 * 商品持久层
 * @author Administrator
 *
 */
public interface GoodsMapper {
	
	/**
	 * 根据商品分类id获取商品的数据列表
	 * @param categoryId 商品分类id
	 * @return 商品的的数据列表
	 */
	List<Goods> findByCategoryId(@Param("categoryId") Long categoryId,
			@Param("offSet") Integer offSet, @Param("count") Integer count);

	/**
	 * 根据商品id获取商品的详情
	 * @param id 商品分类id
	 * @return 商品的的数据
	 */
	Goods findById(Long id);
	
	/**
	 * 根据Priority获取商品的排行
	 * @param count 要显示前几条的数据量
	 * @return 商品的的数据
	 */
	List<Goods> findByPriority(Integer count);
}
