package com.linkage.myshop.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkage.myshop.entity.Cart;
import com.linkage.myshop.service.ICartService;
import com.linkage.myshop.service.util.ResponseResult;
import com.linkage.myshop.vo.CartVO;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController {
	@Autowired
	private ICartService iCartService;
	
	//加入购物车
	@PostMapping("/add_to_cart.do")
	public ResponseResult<Void> handleAddToCart(Cart cart , HttpSession session) {
		Integer uid = getSessionId(session);
		String username = session.getAttribute("username").toString();
		iCartService.insert(cart, uid, username);
		return new ResponseResult<Void>(SUCCESS);
	}	
	
	//删除购物车
	@GetMapping("/delete_data/{ids}")
	public ResponseResult<Void> handleDeleteData(@PathVariable("ids") Integer[] cids , HttpSession session) {
		Integer uid = getSessionId(session);
		iCartService.deleteCart(uid, cids);
		return new ResponseResult<Void>(SUCCESS);
	}	
	
	//修改购物车商品数量
	@PostMapping("/add_count.do")
	public ResponseResult<Void> handleAddCount(Integer id,Boolean flag,HttpSession session) {
		Integer uid = getSessionId(session);
		String username = session.getAttribute("username").toString();
		iCartService.updateCount(uid, id, username, flag);
		return new ResponseResult<>(SUCCESS);
	}	
	
	//查询购物车列表  
	@RequestMapping("/list.do")
	public ResponseResult<List<CartVO>> handleShowList(Cart cart , HttpSession session) {
		Integer uid = getSessionId(session);
		List<CartVO> list = iCartService.getByUid(uid);
		return new ResponseResult<List<CartVO>>(SUCCESS,list);
	}	
	
	//查询订单列表
	@GetMapping("/list")
	public ResponseResult<List<CartVO>> handleList(@RequestParam("cart_id") Integer[] cids){
		List<CartVO> lists = iCartService.getByIds(cids);
		return new ResponseResult<List<CartVO>>(SUCCESS,lists);
	}
	
}
