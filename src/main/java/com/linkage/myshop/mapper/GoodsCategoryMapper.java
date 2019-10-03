package com.linkage.myshop.mapper;

import java.util.List;

import com.linkage.myshop.entity.GoodsCategory;

/**
 * 商品分类持久层
 * @author Administrator
 *
 */
public interface GoodsCategoryMapper {
	
	/**
	 * 根据父级id获取子级商品的分类的数据列表
	 * @param parentId 父级商品的分类id
	 * @return 子集的商品的数据的列表
	 */
	List<GoodsCategory> findByParentId(Long parentId);
}
