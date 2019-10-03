package com.linkage.myshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkage.myshop.entity.Goods;
import com.linkage.myshop.mapper.GoodsMapper;
import com.linkage.myshop.service.IGoodsService;
@Service
public class GoodsServiceImpl implements IGoodsService {
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public List<Goods> getByCategoryId(Long categoryId, Integer offSet, Integer count) {
		return findByCategoryId(categoryId,offSet,count);
	}

	@Override
	public Goods getById(Long id) {
		return findById(id);
	}

	@Override
	public List<Goods> getByPriority(Integer count) {
		return findByPriority(count);
	}

	/**
	 * 根据商品分类id获取商品的数据列表
	 * @param categoryId 商品分类id
	 * @return 商品的的数据列表
	 */
	private List<Goods> findByCategoryId(long categoryId,Integer offSet,Integer count){
		return goodsMapper.findByCategoryId(categoryId, offSet, count);
	}
			
	/**
	 * 根据商品id获取商品的详情
	 * @param id 商品分类id
	 * @return 商品的的数据
	 */
	private Goods findById(long id){
		return goodsMapper.findById(id);
	}
	
	
	/**
	 * 根据Priority获取商品的排行
	 * @param count 要显示前几条的数据量
	 * @return 商品的的数据
	 */
	private List<Goods> findByPriority(Integer count){
		return goodsMapper.findByPriority(count);
	}

}
