package com.linkage.myshop.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkage.myshop.entity.Address;
/**
 * 地址的持久层
 * @author Administrator
 *
 */
public interface AddressMapper {
	/**
	 * 新增地址
	 * @param address 用户传入的地址
	 * @return 插入数据操作后所影响的行数
	 */
	Integer create(Address address);
	
	/**
	 * 根据Uid查找t_address表中含有地址的数量
	 * @param uid 用户id 
	 * @return 返回查找匹配上uid地址的数量
	 */
	Integer countByUid(Integer uid);
	
	/**
	 * 根据uid查找地址信息
	 * @param uid 用户uid
	 * @return 返回查找匹配上uid地址信息的集合，若没有找到则为0
	 */
	List<Address> findByUid(Integer uid);
	
	/**
	 * 根据最后修改时间查找地址信息
	 * @param uid 用户uid
	 * @return 返回查找最新修改时间的地址的aid
	 */
	Integer findByModifiedTime(Integer uid);
	
	/**
	 * 根据aid查找地址信息
	 * @param aid 用户aid
	 * @return 返回查找匹配上aid地址信息
	 */
	Address findByAid(Integer aid);
	
	/**
	 * 根据aid查找地址信息,用于修改地址
	 * @param aid 用户aid
	 * @return 返回查找匹配上aid地址信息
	 */
	Address findAddressByAid(Integer aid);
	
	/**
	 * 把该用户的所用收货地址设为非默认
	 * @param uid 用的uid
	 * @return 返回更新成功数据的行数
	 */
	Integer updateNonDefault (Integer uid);
	
	/**
	 * 把对应的收货地址设为默认
	 * @param aid 地址的id
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 * @return 返回更新成功数据的行数
	 */
	Integer updateDefault (@Param("aid") Integer aid,@Param("modifiedUser") String modifiedUser,@Param("modifiedTime") Date modifiedTime);
	
	/**
	 * 修改地址
	 * @param address 用户传入的地址
	 * @return 插入数据操作后所影响的行数
	 */
	Integer updateByAddress(Address address);
	
	/**
	 * 收货地址删除
	 * @param aid 地址的aid
	 * @return 成功删除的行数
	 */
	Integer delete(Integer aid);
}
