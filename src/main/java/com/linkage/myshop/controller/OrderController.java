package com.linkage.myshop.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.linkage.myshop.entity.Order;
import com.linkage.myshop.service.IOrderService;
/**
 * 订单控制层
 * @author Administrator
 *
 */
import com.linkage.myshop.service.util.ResponseResult;
import com.linkage.myshop.vo.OrderVO;
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
	@Autowired
	private IOrderService iOrderService;
	//创建订单
	@GetMapping("/create.do")
	public void createOrder(@RequestParam("cids") Integer[] cids,@RequestParam("aid") Integer aid,HttpSession session, HttpServletResponse response){
		System.out.println("cids:"+cids);
		System.out.println("aid:"+aid);
		Integer uid = getSessionId(session);
		String username = session.getAttribute("username").toString();
		Order order = iOrderService.addNew(cids, aid, uid, username);
		try {
			response.sendRedirect("../web/payment.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return new ResponseResult<Order>(SUCCESS,order);
	}
	
	//订单详情 
	//http://localhost:8080/order/details/8
	@RequestMapping("/details/{oid}")
	@ResponseBody
	public ResponseResult<OrderVO> handleDetails(@PathVariable("oid") Integer oid){
		OrderVO orderVO = iOrderService.getByOid(oid);
		return new ResponseResult<OrderVO>(SUCCESS,orderVO);
	}
}
