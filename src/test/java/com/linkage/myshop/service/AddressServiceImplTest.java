package com.linkage.myshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.Address;
import com.linkage.myshop.service.exception.ServiceException;
import com.linkage.myshop.service.impl.AddressServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressServiceImplTest {
	@Autowired
	private AddressServiceImpl addressServicesImpl;
	
	@Test
	public void create(){
		Address address = new Address();
		address.setUid(5);
		address.setName("wan");
		address.setProvince("360000");
		address.setCity("360700");
		address.setArea("360721");
		String username="ying";
		System.err.println("begin");
		addressServicesImpl.create(address, username);
		System.err.println("end");
	}
	
	@Test
	public void setDefault(){
		Integer uid=8;
		Integer aid=8;
		String modifiedUser="ab";
		addressServicesImpl.setDefault(uid, aid, modifiedUser);
		System.err.println("end");
	}
	
	@Test
	public void deleteByAid(){
		Integer uid=7;
		Integer aid=12;
		String modifiedUser="ab";
		try {
			addressServicesImpl.deleteByAid(uid, aid, modifiedUser);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
		System.err.println("end");
	}
	
	@Test
	public void getAddressByAid(){
		Integer aid=9;
		Address address = addressServicesImpl.getAddressByAid(aid);
		System.err.println("end"+address);
	}
	
	@Test
	public void updateAddress(){
		Address address = new Address();
		address.setAid(13);
		address.setName("wani");
		address.setProvince("360000");
		address.setCity("360700");
		address.setArea("360721");
		String username="ying";
		addressServicesImpl.updateAddress(address, username);
		System.err.println("end");
	}

}
