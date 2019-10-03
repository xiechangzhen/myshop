package com.linkage.myshop.service;

import java.util.List;

import com.linkage.myshop.entity.Address;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.AddressNotFoundException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.UpdateException;

/**
 * 地址的业务层接口
 * @author Administrator
 *
 */
public interface IAddressService {
	
	/**
	 * 新增地址
	 * @param address 用户传入的地址
	 * @throws InsertException 新增地址异常
	 */
	void create(Address address ,String username) throws InsertException;

	
	/**
	 * 根据uid查找地址信息
	 * @param uid 用户uid
	 * @return 返回查找匹配上uid地址信息的集合，若没有找到则为0
	 */
	List<Address> findByUid(Integer uid);
	
	
	/**
	 * 根据aid查找地址信息,用于修改信息
	 * @param aid 用户aid
	 * @return 返回查找匹配上aid地址信息
	 */
	Address getAddressByAid(Integer aid);
	
	/**
	 * 设置默认地址
	 * @param uid 用户uid
	 * @param aid 地址aid
	 * @param modifiedUser 修改人
	 * @throws UpdateException 更新异常，设置默认地址失败
	 */
	void setDefault(Integer uid, Integer aid,String modifiedUser) throws AddressNotFoundException,AccessDenisedException;
	
	
	/**
	 * 修改地址
	 * @param address 用户传入的地址
	 * @throws InsertException 新增地址异常
	 */
	void updateAddress(Address address ,String username) throws UpdateException;
	
	/**
	 * 删除地址
	 * @param uid 用户uid
	 * @param aid 地址aid
	 * @param modifiedUser 修改人
	 */
	void deleteByAid(Integer uid, Integer aid,String modifiedUser) throws AddressNotFoundException , AccessDenisedException;
}
