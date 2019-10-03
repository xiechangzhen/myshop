package com.linkage.myshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkage.myshop.entity.District;
import com.linkage.myshop.service.IDistrictService;
import com.linkage.myshop.service.util.ResponseResult;
/**
 * 控制层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/district")
public class DistrictController extends BaseController{
	@Autowired
	private IDistrictService districtService;
//	@RequestMapping("/list.do")  省市区查询显示
	//Representational State Transfer 
	@RequestMapping("/list/{parent}")
	public ResponseResult<List<District>> handleShow(@PathVariable("parent") String parent){
		System.err.println("come in district");
		List<District> districts = districtService.getByParent(parent);
		return new ResponseResult<List<District>>(SUCCESS,districts);
	}

}
