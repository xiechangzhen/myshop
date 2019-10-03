package com.linkage.myshop.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.linkage.myshop.entity.User;
/**
 * 持久层接口
 * @author Administrator
 *
 */

@Mapper
public interface UserMapper {
	/**
	 * 插入用户数据
	 * @param user 用户名
	 * @return 受影响的函数
	 */
	Integer addNew(User user);
	
	/**
	 * 根据用户名查询用户数据
	 * @param username 用户名
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	User findByUsername(String username);
	
	/**
	 * 根据用户id查询用户数据
	 * @param id 用户id
	 * @return  匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	User findById(Integer id);
	
	/**
	 * 根据id修改用户密码
	 * @param 用户id
	 * @param password	修改后的密码
	 * @param modifiedUser 修改者
	 * @param modifiedTime	修改时间
	 * @return 更新数据的行数
	 */
	Integer changePassword(@Param("id") Integer id,@Param("password") String password ,@Param("modifiedUser") String modifiedUser ,@Param("modifiedTime") Date modifiedTime );
	
	/**
	 * 修改用户信息							
	 * @param user 用户提交的信息
	 * @return 返回影响结果的行数
	 */
	Integer updateInfo(User user);
		
	/**
	 * 更新头像
	 * @param id 用户id
	 * @param avatar 头像的地址
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 * @return
	 */
	Integer updateAvatar(@Param("id") Integer id,@Param("avatar") String avatar,@Param("modifiedUser") String modifiedUser,@Param("modifiedTime") Date modifiedTime);
								
}
