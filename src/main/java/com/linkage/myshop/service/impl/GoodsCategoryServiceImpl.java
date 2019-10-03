package com.linkage.myshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkage.myshop.entity.GoodsCategory;
import com.linkage.myshop.mapper.GoodsCategoryMapper;
import com.linkage.myshop.service.IGoodsCategoryService;
@Service
public class GoodsCategoryServiceImpl implements IGoodsCategoryService{
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
	@Override
	public List<GoodsCategory> getByParentId(Long parentId) {
		System.err.println("getByParentId");
		return findByParentId(parentId);
	}
	
	/**
	 * 根据父级id获取子级商品的分类的数据列表
	 * @param parentId 父级商品的分类id
	 * @return 子集的商品的数据的列表
	 */
	private List<GoodsCategory> findByParentId(Long parentId){
		return goodsCategoryMapper.findByParentId(parentId);
	}
	
}
