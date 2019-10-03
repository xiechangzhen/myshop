package com.linkage.myshop.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkage.myshop.entity.District;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistrictMapperTest {
	@Autowired
	private DistrictMapper districtMapper;
	
	@Test
	public void getByParentTest(){
		String parent ="86";
		System.out.println("begin:");
		List<District> district = districtMapper.findByParent(parent);
		for (District district2 : district) {
			System.out.println("district:"+district2.toString());
		}
		System.out.println("end:");
		
	}
	
	@Test
	public void findByCodyTest(){
		String code ="360000";
		System.out.println("begin:");
		District district = districtMapper.findByCody(code);
		System.err.println("end:"+district.toString());
		
	}
}
