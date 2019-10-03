package com.linkage.myshop.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Address;
import com.linkage.myshop.service.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressMapperTest {
	@Autowired
	private AddressMapper addressMapper;
	
	@Test
	public void  create(){
		Address address = new Address();
		address.setUid(5);
		address.setName("云");
		address.setProvince("江西");
		addressMapper.create(address);
	}
	
	/**
	 * 根据Uid查找t_address表中含有地址的数量
	 * @param uid 用户id 
	 * @return 返回查找匹配上uid地址的数量
	 */
	@Test
	public void  countByUid(){
		Integer rows = addressMapper.countByUid(8);
		System.err.println("rows:"+rows);
		
	}
	
	@Test
	public void  findByUid(){
		List<Address> lists = addressMapper.findByUid(7);
		for (Address address : lists) {
			System.err.println("address:"+address.toString());
		}
		System.out.println("end");
	}
	
	@Test
	public void  updateNonDefault(){
		Integer rows = addressMapper.updateNonDefault(7);
		System.out.println("rows:"+rows);
		System.out.println("end");
	}
	
	@Test
	public void  updateDefault(){
		Date date = new Date();
		Integer rows = addressMapper.updateDefault(9,"ab",date);
		System.out.println("rows:"+rows);
		System.out.println("end");
	}
	
	@Test
	public void  findByModifiedTime(){
		Integer aid = addressMapper.findByModifiedTime(7);
		System.out.println("aid:"+aid);
		System.out.println("end");
	}
	
	@Test
	public void  deleteByAid(){
		Integer aid = 10;
		try {
			Integer rows = addressMapper.delete(aid);
			
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
		//System.out.println("rows:"+rows);
		System.out.println("end");
	}
	
	@Test
	public void  deleteByAid1(){
		StringBuffer sb = new StringBuffer("abcde");
		sb.insert(1, '@');
		sb.setCharAt(3, 'A');
		sb.setLength(3);
		System.out.println("rows:"+sb);
		System.out.println("end");
	}
	
}
