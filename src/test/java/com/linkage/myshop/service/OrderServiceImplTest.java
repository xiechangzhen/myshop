package com.linkage.myshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.service.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
	@Autowired
	private IOrderService iOrderService;
	@Test
	public void addNew(){
		try {
			Integer [] cids = {12,17};
			Integer aid= 16;
			Integer uid= 7;
			String username= "me";
			iOrderService.addNew(cids, aid, uid, username);
			System.err.println("end");
			
		} catch (ServiceException e) {
			System.err.println(e.getMessage());
		}
	}
	@Test
	public void getByOid(){
			Integer oid= 8;
			iOrderService.getByOid(oid);
			System.err.println("end");
	}

}
