package com.linkage.myshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkage.myshop.entity.GoodsCategory;
import com.linkage.myshop.service.IGoodsCategoryService;
import com.linkage.myshop.service.util.ResponseResult;

@RestController
@RequestMapping("category")
public class GoodsCategoryController extends BaseController{
	
	@Autowired
	private IGoodsCategoryService iGoodsCategoryService;
	
	@GetMapping("/list/{parentId}")
	public ResponseResult<List<GoodsCategory>> handleList(@PathVariable("parentId") Long parentId){
		List<GoodsCategory> list = iGoodsCategoryService.getByParentId(parentId);
		return new ResponseResult<List<GoodsCategory>>(SUCCESS,list);
	}
	
}
