package com.linkage.myshop.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkage.myshop.entity.Address;
import com.linkage.myshop.service.IAddressService;
import com.linkage.myshop.service.util.ResponseResult;
/**
 * 控制层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/address")
public class AddressController extends BaseController{
	@Autowired
	private IAddressService iAddressService;
	//增加地址
	@PostMapping("/add_address.do")
	public ResponseResult<Void> handleReg(Address address,HttpSession session){
		Integer uid = getSessionId(session);
		String username = session.getAttribute("username").toString();
		address.setUid(uid);
		iAddressService.create(address, username);
		return new ResponseResult<Void>(SUCCESS);
	}
	
	//显示地址列表
	@RequestMapping("/list")
	public ResponseResult<List<Address>> handleList(HttpSession session){
		Integer uid = getSessionId(session);
		List<Address> addresses= iAddressService.findByUid(uid);
		return new ResponseResult<List<Address>>(SUCCESS,addresses);
	}
	
	//设置默认地址
	@GetMapping("/default/{aid}")
	public ResponseResult<Void> handleDefault(@PathVariable("aid") Integer aid, HttpSession session){
		Integer uid = getSessionId(session);
		String username = session.getAttribute("username").toString();
		iAddressService.setDefault(uid, aid, username);
		return new ResponseResult<Void>(SUCCESS);
	}
	
	//删除地址
	@GetMapping("/delete/{aid}")
	public ResponseResult<Void> handleDelete(@PathVariable("aid") Integer aid, HttpSession session){
		Integer uid = getSessionId(session);
		String username = session.getAttribute("username").toString();
		iAddressService.deleteByAid(uid, aid, username);
		return new ResponseResult<Void>(SUCCESS);
	}
	
	//编辑地址
	@GetMapping("/edit/{aid}")
	public ResponseResult<Address> handleEdit(@PathVariable("aid") Integer aid, HttpSession session){
		Address address = iAddressService.getAddressByAid(aid);
		return new ResponseResult<Address>(SUCCESS,address);
	}
	
	//修改地址
	@PostMapping("/update_address.do")
	public ResponseResult<Void> handleEdit(Address address, HttpSession session){
		String username = session.getAttribute("username").toString();
		iAddressService.updateAddress(address, username);
		return new ResponseResult<Void>(SUCCESS);
	}
}
