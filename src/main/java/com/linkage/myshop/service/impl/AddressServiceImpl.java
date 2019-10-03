package com.linkage.myshop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkage.myshop.entity.Address;
import com.linkage.myshop.entity.District;
import com.linkage.myshop.mapper.AddressMapper;
import com.linkage.myshop.service.IAddressService;
import com.linkage.myshop.service.IDistrictService;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.AddressNotFoundException;
import com.linkage.myshop.service.exception.DeleteException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.UpdateException;

@Service
public class AddressServiceImpl implements IAddressService {

	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private IDistrictService iDistrictService;
	
	@Override
	public void create(Address address,String username) {
		String district = getDistrict(address.getProvince(),address.getCity(),address.getArea());
		address.setDistrict(district);
		//查看用户有多少条地址数据
		Integer counts = getCountByUid(address.getUid());
		//若没有数据，则设置为默认地址（1），否则设置为0
		address.setIsDefault((counts == 0 )? 1:0);
		//生成日期
		Date date = new Date();
		//设置创建地址的人
		address.setCreateUser(username);
		//设置修改地址的人
		address.setModifiedUser(username);
		//设置创建地址的时间
		address.setCreateTime(date);
		//设置修改地址的时间
		address.setModifiedTime(date);
		//插入数据
		createAddress(address);
	}

	@Override
	public void updateAddress(Address address, String username) {
		String district = getDistrict(address.getProvince(),address.getCity(),address.getArea());
		address.setDistrict(district);
		//生成日期
		Date date = new Date();
		//设置修改地址的人
		address.setModifiedUser(username);
		//设置修改地址的时间
		address.setModifiedTime(date);
		//插入数据
		updateByAddress(address);
	}
	
	@Override
	public List<Address> findByUid(Integer uid) {
		return getByUid(uid);
	}
	
	@Override
	public Address getAddressByAid(Integer aid) {
		return findAddressByAid(aid);
	}
	
	@Override
	@Transactional
	public void setDefault(Integer uid, Integer aid,String modifiedUser) throws AddressNotFoundException,AccessDenisedException{
		//查看是否含有aid的收货地址
		Address address = getByAid(aid);
		//检查数据是否为null
		if(address == null){
			//是：抛出AddressNotFoundException
			throw new AddressNotFoundException("设置为默认收货地址失败，该收货地址不存在");
		}
		//检查数据归属是否有误
		if(!address.getUid().equals(uid)){
			//是：抛出AccessDenisedException
			throw new AccessDenisedException("设置为默认收货地址失败，没有修改权限！");
		}
		//全部改为非默认
		updateNonDefault(uid);
		//生成修改时间
		Date  modifiedTime= new Date();
		//把aid的地址该改为默认的地址
		updateDefault(aid,modifiedUser,modifiedTime);
	}
	
	@Override
	@Transactional
	public void deleteByAid(Integer uid, Integer aid,String modifiedUser) throws AddressNotFoundException , AccessDenisedException{
		//查看是否含有aid的收货地址
		Address address = getByAid(aid);
		//检查数据是否为null
		if(address == null){
			//是：抛出AddressNotFoundException
			throw new AddressNotFoundException("删除收货地址失败，该收货地址不存在");
		}
		//检查数据归属是否有误
		if(!address.getUid().equals(uid)){
			//是：抛出AccessDenisedException
			throw new AccessDenisedException("删除收货地址失败，没有修改权限！");
		}
		//执行删除
		delete(aid);
		//检查是否含有收货地址:getCountByUid()
		Integer count = getCountByUid(uid);
		//有：判断删除的是否为默认的地址，
		if(count > 0 && address.getIsDefault()==1){
			//是：获取最后修改的地址
			//把最后修改的地址设为默认的地址
			Integer newAid = findByModifiedTime(uid);
			setDefault(uid, newAid, modifiedUser);
		}
	}
	
	/**
	 * 新增地址
	 * @param address 用户传入的地址
	 * @return 插入数据操作后所影响的行数
	 */
	 private void createAddress(Address address) throws InsertException{
		Integer rows = addressMapper.create(address);
		if(rows != 1){
			throw new InsertException("新增地址异常");
		}
	 }
	
	/**
	 * 根据Uid查找t_address表中含有地址的数量
	 * @param uid 用户id 
	 * @return 返回查找匹配上uid地址的数量
	 */
	 private Integer getCountByUid(Integer uid){
		return addressMapper.countByUid(uid);
	 }
	 
	 /**
	  * 得到省市区拼接后的名字
	  * @param province 省代号
	  * @param city 市的代号
	  * @param area
	  * @return 返回省市区拼接后的名字
	  */
	 private String getDistrict(String province,String city,String area){
		 String provinceName=null;
		 String cityName=null;
		 String areaName=null;
		 District p = iDistrictService.getByCody(province);
		 District c = iDistrictService.getByCody(city);
		 District a = iDistrictService.getByCody(area);
		 if(p != null){
			 provinceName = p.getName();
		 }
		 if(c != null){
			 cityName = c.getName();
		 }
		 if(a != null){
			 areaName = a.getName();
		 }
		return provinceName+cityName+areaName;
	 }

	 /**
	  *  得到省市区拼接后的名字
	  * @param uid 用户uid
	  * @return 返回查找匹配上uid地址信息的集合，若没有找到则为0
	  */
	 private List<Address> getByUid(Integer uid) {
		return addressMapper.findByUid(uid);
	}
	 
	/**
	 * 根据aid查找地址信息
	 * @param aid 用户aid
	 * @return 返回查找匹配上aid地址信息
	 */
	 private Address getByAid(Integer aid) {
		 return addressMapper.findByAid(aid);
	 }

	 /**
	  * 把该用户的所用收货地址设为非默认
	  * @param uid 用的uid
	  */
	 private void updateNonDefault (Integer uid){
		 Integer rows = addressMapper.updateNonDefault(uid);
		 if(rows < 1){
			throw new UpdateException("设为收货地址为默认时更新数据失败");
		 }
	 }

	/**
	 * 把对应的收货地址设为默认
	 * @param aid 地址的id
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 */
	 private void updateDefault (Integer aid,String modifiedUser,Date modifiedTime) throws UpdateException{
		 Integer rows = addressMapper.updateDefault(aid,modifiedUser,modifiedTime);
		 if(rows != 1){
			throw new UpdateException("设为收货地址为默认时失败");
		 }
	 }
		
	
	/**
	 * 修改收货地址
	 * @param address 收货地址
	 * @throws UpdateException 收货地址修改失败异常
	 */
	 private void updateByAddress (Address address) throws UpdateException{
		 Integer rows = addressMapper.updateByAddress(address);
		 if(rows != 1){
			 throw new UpdateException("收货地址为修改失败");
		 }
	 }

	/**
	 * 根据最后修改时间查找地址信息
	 * @param uid 用户uid
	 * @return 返回查找最新修改时间的地址的aid
	 */
	private Integer findByModifiedTime(Integer uid){
		return addressMapper.findByModifiedTime(uid);
	}
	
	/**
	 * 收货地址删除
	 * @param aid 地址的aid
	 * @return 成功删除的行数
	 */
	private void delete(Integer aid) throws DeleteException{
		Integer rows = addressMapper.delete(aid);
		if(rows != 1){
			throw new  DeleteException("删除收货地址失败");
		}
	}

	/**
	 * 通过aid查找收货地址
	 * @param aid 地址的aid
	 * @return 匹配上的收货地址
	 */
	private Address findAddressByAid(Integer aid){
		return addressMapper.findAddressByAid(aid);
	}


}
