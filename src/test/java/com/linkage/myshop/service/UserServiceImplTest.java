package com.linkage.myshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.User;
import com.linkage.myshop.service.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
	@Autowired
	private IUserService iUserService;
	
	@Test
	public void regTest(){
		try {
			User user = new User();
			user.setUsername("ab");
			user.setPassword("123");
			user.setGender(0);
			user.setPhone("18195956838");
			user.setEmail("128135@qq.com");
			user.setAvatar("img/12.jpg");
			User data = iUserService.reg(user);
			System.err.println("UserServiceImplTest-data:"+data.toString());
			
		} catch (ServiceException e) {
			System.out.println("错误类名："+e.getClass().getName());
			System.out.println("错误信息："+e.getMessage());
		}
	}
	
	@Test
	public void login(){
		try {
			iUserService.login("ab", "123");
		} catch (ServiceException e) {
			System.out.println("错误类名："+e.getClass().getName());
			System.out.println("错误信息："+e.getMessage());
		}
	}
	
	@Test
	public void changePassword(){
		try {
			Integer id = 7;
			String oldPassword = "1235";
			String newPassword = "1234";
			String modifiedUser = "admin1";
			iUserService.changePassword(id, oldPassword, newPassword, modifiedUser);
		} catch (ServiceException e) {
			System.err.println("错误类名："+e.getClass().getName());
			System.err.println("错误信息："+e.getMessage());
		}
	}
	
	@Test
	public void a(){
		Integer aa =200;
		Integer bb =200;
		if(aa==bb){
			System.out.println(1);
		}else{
			System.out.println(0);
		}
	}
}
