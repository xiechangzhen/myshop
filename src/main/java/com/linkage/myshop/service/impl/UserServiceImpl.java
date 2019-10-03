package com.linkage.myshop.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.linkage.myshop.entity.User;
import com.linkage.myshop.mapper.UserMapper;
import com.linkage.myshop.service.IUserService;
import com.linkage.myshop.service.exception.DuplicateKeyException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.PasswordNotMatchException;
import com.linkage.myshop.service.exception.UpdateException;
import com.linkage.myshop.service.exception.UsernameNotFoundException;
/**
 * 处理用户数据的业务层实现类
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User reg(User user) throws DuplicateKeyException, InsertException {
		//根据尝试注册的用户名查询用户数据
		User data = findByUsername(user.getUsername());
		//判断查询的数据是否为null
		if(data == null){
			//是：用户名不存在，允许注册，则处理密码加密
			//加密-1:获取随机的UUID作为盐值
			String uuid = UUID.randomUUID().toString().toUpperCase();
			//加密-2:获取用户提交的原始密码
			String srcPassword = user.getPassword();
			//加密-3:基于原始密码和盐值执行加密
			String md5Password = getMD5Password(uuid,srcPassword);
			//加密-4:将加密后的密码封装在 user对象中
			user.setPassword(md5Password);
			//用户注册时随机生成的盐值
			user.setSalt(uuid);
			//是否已经删除：否
			user.setIsDelete(0);
			//4项日志
			user.setCreateUser(user.getUsername());
			user.setModifiedUser(user.getUsername());
			Date date = new Date();
			user.setCreateTime(date);
			user.setModifiedTime(date);
			//执行注册
			addNew(user);
			//返回注册的用户对象
			return user;
		}else{
			//否：用户存名已经被占用，抛出
			throw new DuplicateKeyException("注册失败，尝试注册的用户名："+user.getUsername()+"已经存在");
		}
	}

	@Override
	public User login(String username, String password) throws UsernameNotFoundException,PasswordNotMatchException{
		//根据用户名查找用户
		User user = findByUsername(username);
		//结果是否为null 是 
		if(user == null){
			//抛出用户名不存在异常
			throw new UsernameNotFoundException("登录失败，尝试登录的用户名："+username+"不存在");
		}
		//结果是否为null 否
		//查询用户是否被删除 
		if(user.getIsDelete() == 1 ){
			//是 （ 用户已经删除） ，抛出用户未找到异常
			throw new UsernameNotFoundException("登录失败，尝试登录的用户名："+username+"不存在");
		}
		//取出用户的盐值
		String salt = user.getSalt();
		//对用户输入的密码进行加密
		String md5Password = getMD5Password(salt,password);
		//验证密码是否正确
		if(!user.getPassword().equals(md5Password)){
			//否：密码错误 抛异常
			throw new PasswordNotMatchException("登录密码错误");
		}
		//是，登录
		//把颜值清空返回
		user.setSalt(null);
		//把密码清空返回
		user.setPassword(null);
		return user;
	}
	
	@Override
	public void changePassword(Integer id, String oldPassword, String newPassword,String modifiedUser)
			throws UsernameNotFoundException,PasswordNotMatchException{
		//根据用户id查询用户的信息，
		User user = findById(id);
		//结果是否为null 是 
		if(user == null){
			//抛出用户名不存在异常
			throw new UsernameNotFoundException("修改密码失败，该用户名不存在");
		}
		//结果是否为null 否
		//查询用户是否被删除 
		if(user.getIsDelete() == 1 ){
			//是 （ 用户已经删除） ，抛出用户未找到异常
			throw new UsernameNotFoundException("修改密码失败，该用户名不存在");
		}
		//获取数据库中用户的盐值
		String salt = user.getSalt();
		//获取数据库中密码
		String passwordDB = user.getPassword();
		//给输入的原密码加密
		String oldMD5Password = getMD5Password(salt, oldPassword);
		//是否和数据库中的密码一致
		if(!passwordDB.equals(oldMD5Password)){
			throw new PasswordNotMatchException("要修改原密码不匹配");
		}
		//给新密码加密
		String newMD5Password = getMD5Password(salt, newPassword);
		//生成修改密码的时间
		Date modifiedTime = new Date();
		//把密码存入和修改者，修改时间存入数据库
		changePassword(id, newMD5Password, modifiedUser, modifiedTime);
	}

	@Override
	public void changeInfo(User user) throws UsernameNotFoundException{
		//根据用户id查询用户的信息，
		User userDB = findById(user.getId());
		//结果是否为null 是 
		if(userDB == null){
			//抛出用户名不存在异常
			throw new UsernameNotFoundException("修改信息失败，该用户名不存在");
		}
		//结果是否为null 否
		//查询用户是否被删除 
		if(userDB.getIsDelete() == 1 ){
			//是 （ 用户已经删除） ，抛出用户未找到异常
			throw new UsernameNotFoundException("修改信息失败，该用户名不存在");
		}
		
		userDB.setId(user.getId());
		userDB.setPhone(user.getPhone());
		userDB.setEmail(user.getEmail());
		userDB.setGender(user.getGender());
		String modifiedUser = user.getUsername();
		userDB.setModifiedUser(modifiedUser);
		Date modifiedTime = new Date();
		userDB.setModifiedTime(modifiedTime);
		System.err.println("userDB:"+userDB.toString());
		updateInfo(userDB);
	}
	
	@Override
	public void updateAvatar(Integer id,String username,String avatar) throws UsernameNotFoundException {
		//根据用户id查询用户的信息，
		User userDB = findById(id);
		//结果是否为null 是 
		if(userDB == null){
			//抛出用户名不存在异常
			throw new UsernameNotFoundException("修改头像信息失败，该用户名不存在");
		}
		//结果是否为null 否
		//查询用户是否被删除 
		if(userDB.getIsDelete() == 1 ){
			//是 （ 用户已经删除） ，抛出用户未找到异常
			throw new UsernameNotFoundException("修改头像信息失败，该用户名不存在");
		}
		Date modifiedTime = new Date();
		updateAvatarInfo(id,avatar,username,modifiedTime);
	}
	
	@Override
	public User showUserData(Integer id) throws UsernameNotFoundException {
		/*
		 * 在返回之前，将返回对象中的关键数据（不希望响应给客户的数据）字段设置为null即可；
		 */
		User userDB = findById(id);
		//结果是否为null 是 
		if(userDB == null){
			//抛出用户名不存在异常
			throw new UsernameNotFoundException("显示用户信息失败，该用户名不存在");
		}
		//结果是否为null 否
		//查询用户是否被删除 
		if(userDB.getIsDelete() == 1 ){
			//是 （ 用户已经删除） ，抛出用户未找到异常
			throw new UsernameNotFoundException("显示用户信息失败，该用户名不存在");
		}
		userDB.setSalt(null);
		userDB.setPassword(null);
		userDB.setIsDelete(null);
		return userDB;
	}
	
	/**
	 * 插入用户数据
	 * @param user 用户信息
	 */
	private void addNew(User user){
		Integer rows = userMapper.addNew(user);
		if(rows != 1){
			throw new InsertException("增加用户数据时出现未知错误！");
		}
	}
	
	/**
	 * 根据用户名查找用户
	 * @param username 用户名
	 * @return 查询到的用户信息
	 */
	private User findByUsername(String username){
		return userMapper.findByUsername(username);
	}
	
	/**
	 * 给用户密码加密
	 * @param uuid  盐值
	 * @param srcPassword 用户输入的原密码
	 * @return 加密后的密码字符串
	 */
	private String getMD5Password(String uuid,String srcPassword){
		//以下加密规则自由设计
		//盐值 拼接 原密码 拼接 盐值
		String str = uuid + srcPassword + uuid;
		//循环执行10次摘要运算
		for (int i = 0; i < 10; i++) {
			str = DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
		}
		//返回摘要结果
		return str;
	}

	/**
	 * 根据id查找用户
	 * @param id 修改密码的id
	 * @return 根据id查出的用户信息
	 */
	private User findById(Integer id){
		return userMapper.findById(id);
	}
	
	/**
	 * 修改密码
	 * @param id 用户id
	 * @param password 用户密码
	 * @param modifiedUser	修改者
	 * @param modifiedTime	修改时间
	 * @throws UpdateException	密码更新失败，出现未知错误
	 */
	private void changePassword (Integer id,String password ,String modifiedUser,Date modifiedTime) throws UpdateException{
		 Integer rows = userMapper.changePassword(id, password, modifiedUser, modifiedTime);
		 if(rows != 1){
			 throw new UpdateException("密码更新失败，出现未知错误");
		 }
	 }

	/**
	 * 
	 * @param user
	 * @throws UpdateException
	 */
	private void updateInfo(User user) throws UpdateException{
		System.out.println("updateInfo:-id:"+user.getId());
		Integer rows = userMapper.updateInfo(user);
		if(rows != 1){
			throw new UpdateException("用户信息更新失败，出现未知错误");
		}
	}
	
	/**
	 * 修改用户信息
	 * @param id 用户id
	 * @param avatar 头像的地址
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 * @throws UpdateException 用户头像更新失败，出现未知错误
	 */
	private void updateAvatarInfo(Integer id, String avatar,String modifiedUser,Date modifiedTime) throws UpdateException{
		Integer rows = userMapper.updateAvatar(id,avatar,modifiedUser,modifiedTime);
		if(rows != 1){
			throw new UpdateException("用户头像更新失败，出现未知错误");
		}
	}

	




}
