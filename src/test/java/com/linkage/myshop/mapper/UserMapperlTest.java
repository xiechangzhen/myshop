package com.linkage.myshop.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperlTest {
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void findByIdTest(){
		Integer id = 6;
		User user = userMapper.findById(id);
		System.err.println("UserMapperTest-findByIdTest-user:"+user);
	}
	
	@Test
	public void changePassword(){
		Integer id = 6;
		String password="123";
		String modifiedUser="admin";
		Date modifiedTime = new Date();
		Integer rows = userMapper.changePassword(id,password,modifiedUser,modifiedTime);
		System.err.println("UserMapperTest-changePassword-rows:"+rows);
		
	}
	
	@Test
	public void changeAvatar(){
		Integer id = 7;
		String avatar="D:upload/1.jpg";
		String modifiedUser="admin";
		Date modifiedTime = new Date();
		Integer rows = userMapper.updateAvatar(id,avatar,modifiedUser,modifiedTime);
		System.err.println("UserMapperTest-changeAvatar-rows:"+rows);
		
	}
	
}	
