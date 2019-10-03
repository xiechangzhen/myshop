package com.linkage.myshop.service;


import org.springframework.dao.DuplicateKeyException;

import com.linkage.myshop.entity.User;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.PasswordNotMatchException;
import com.linkage.myshop.service.exception.UpdateException;
import com.linkage.myshop.service.exception.UsernameNotFoundException;
/**
 * 业务层 接口
 * @author Administrator
 *
 */
public interface IUserService {
	/**
	 * 用户注册
	 * @param user 用户注册的信息
	 * @return 成功注册的用户
	 * @throws DuplicateKeyException 键唯一约束异常
	 * @throws InsertException 插入异常
	 */
	User reg(User user) throws DuplicateKeyException,InsertException;
	
	/**
	 * 根据用户名用户和密码登录
	 * @param username 用户名
	 * @param password 密码
	 * @return 查询的用户，如果查不到返回为null
	 */
	User login (String username,String password) throws UsernameNotFoundException,PasswordNotMatchException;
	
	/**
	 * 用户修改密码
	 * @param id 用户id
	 * @param oldPassword 修改的原密码
	 * @param newPassword 修改的新密码
	 * @throws UsernameNotFoundException 用户名找不到
	 * @throws PasswordNotMatchException 密码不匹配
	 * @throws UpdateException 密码更新异常
	 */
	void changePassword(Integer id,String oldPassword,String newPassword, String modifiedUser) throws UsernameNotFoundException,PasswordNotMatchException,UpdateException ;

	/**
	 * 修改用户信息
	 * @param user 用户修改后提交的信息
	 * @throws UsernameNotFoundException 用户名未找到异常
	 * @throws UpdateException 更新异常
	 */
	void changeInfo(User user) throws UsernameNotFoundException,UpdateException;
	
	/**
	 * 显示用户信息
	 * @param id 用户id
	 * @throws UsernameNotFoundException 用户名未找到异常
	 */
	User showUserData(Integer id) throws UsernameNotFoundException;
	
	/**
	 * 修改用户头像
	 * @param id
	 * @param avatar
	 * @throws UpdateException
	 */
	void updateAvatar(Integer id ,String username ,String avatar) throws UsernameNotFoundException, UpdateException;
}
