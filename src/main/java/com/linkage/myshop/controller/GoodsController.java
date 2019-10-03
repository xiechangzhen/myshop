package com.linkage.myshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkage.myshop.entity.Goods;
import com.linkage.myshop.service.IGoodsService;
import com.linkage.myshop.service.util.ResponseResult;
/**
 * 商品控制层
 * @author Administrator
 *
 */

@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {
	
	@Autowired
	private IGoodsService iGoodsService;
	
	
	@GetMapping("/list/{categoryId}")
	public ResponseResult<List<Goods>> getByCategoryId(@PathVariable("categoryId") Long categoryId){
		List<Goods> list = iGoodsService.getByCategoryId(categoryId, 0, 20);
		return new ResponseResult<List<Goods>>(SUCCESS,list);
	}
	
	//查看商品详情
	@GetMapping("/details/{id}")
	public ResponseResult<Goods> getById(@PathVariable("id") Long id){
		Goods goods = iGoodsService.getById(id);
		return new ResponseResult<Goods>(SUCCESS,goods);
	}
	
	//热销排行
	@GetMapping("/hot.do")
	public ResponseResult<List<Goods>> hotGoods(){
		//取优先级前4条数据显示（可自己制定）
		Integer count = 4;
		List<Goods> goods = iGoodsService.getByPriority(count);
		return new ResponseResult<List<Goods>>(SUCCESS,goods);
	}
}
