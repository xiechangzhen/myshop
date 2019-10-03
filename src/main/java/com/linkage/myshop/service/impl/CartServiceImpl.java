package com.linkage.myshop.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkage.myshop.entity.Cart;
import com.linkage.myshop.mapper.CartMapper;
import com.linkage.myshop.service.ICartService;
import com.linkage.myshop.service.exception.AccessDenisedException;
import com.linkage.myshop.service.exception.CartNotFoundException;
import com.linkage.myshop.service.exception.CountLimitException;
import com.linkage.myshop.service.exception.DeleteException;
import com.linkage.myshop.service.exception.InsertException;
import com.linkage.myshop.service.exception.UpdateException;
import com.linkage.myshop.vo.CartVO;
/**
 * 购物车业务层实现类
 * @author Administrator
 *
 */
@Service
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private CartMapper cartMapper;
	
	//加入购物车
	@Override
	public void insert(Cart cart,Integer uid,String username) throws InsertException , UpdateException{
		//把传入进来的购物车的uid（用户的id）和gid（商品的id）得到
		Long gid = cart.getGid();
		//生成修改时间	
		Date date = new Date();
		//查询数据库中购物车该商品信息
		Cart cartDB = findByUidAndGid(uid,gid);
		//是否含有该条数据
		if(cartDB == null){
			//没有该条信息，设置uid
			cart.setUid(uid);
			//设置创建人
			cart.setCreateUser(username);
			//设置创建时间
			cart.setCreateTime(date);
			//设置修改人
			cart.setModifiedUser(username);
			//设置修改时间
			cart.setModifiedTime(date);
			//进行插入操作
			addNew(cart);
		}else{
			//存在该条信息，得到数据数据库中该商品的数量
			Integer count = cartDB.getCount();
			//把传入的商品的数量和数据库中的商品的数据相加得到新的数量
			count += cart.getCount();
			//得到购物车中该数据的id
			Integer id = cartDB.getId();
			//进行更新数量的操作
			update( id,  count,  username,  date);
		}
		
	}

	//删除购物车
	@Override
	public void deleteCart(Integer uid,Integer[] cids) throws DeleteException,CartNotFoundException, AccessDenisedException{
		//查看数组中id的信息
		for(int i=0;i <cids.length; i++){
			//查询数据库中购物车该商品信息
			Cart cartDB = findById(cids[i]);
			//是否含有该条数据
			if(cartDB == null){
				throw new CartNotFoundException("删除数据失败，该购物车数据不存在");
			}
			//查看该条数据uid是否为当前用户的uid
			if(!cartDB.getUid().equals(uid)){
				//不是，则抛出访问权限异常!
				throw new AccessDenisedException("删除商品数量失败，访问权限异常!");
			}
		}
		//删除购物车数据
		deleteByIds(cids);
	}

	@Override
	public List<CartVO> getByUid(Integer uid) {
		return findByUid(uid);
	}
	
	@Override
	public List<CartVO> getByIds(Integer[] gids) {
		return findByIds(gids);
	}
	
	@Override
	public void updateCount(Integer uid,Integer id,String username,Boolean flag) throws CartNotFoundException,
	AccessDenisedException,UpdateException,CountLimitException{
		//根据购物车id查询购物车数据
		Cart cart= findById(id);
		//判断是否为空
		if(cart == null){
			//是：抛出购物车找不到异常
			throw new  CartNotFoundException("修改商品数量失败，购物车找不到异常!");
		}
		//查看该条数据uid是否为当前用户的uid
		if(!cart.getUid().equals(uid)){
			//不是，则抛出访问权限异常!
			throw new AccessDenisedException("修改商品数量失败，访问权限异常!");
		}
		//获取数据库中的原有的商品数量
		Integer count = cart.getCount();
		//如果为真
		if(flag){
			//数量加一
			
			count++;
			System.out.println("count:"+count);
		}else{
			//判断购物车数据中商品数量是否为小于1
			if(count<1){
				throw new CountLimitException("减少商品数量失败，数量限制异常!");
			}
			//数量大于1时，则数量可以减去1
			count--;
			System.out.println("reduce");
		}
		//生成修改时间
		Date now = new Date();
		//增加商品数量
		System.out.println("uid:"+uid+"count:"+count);
		update(id, count, username, now);
	}

	/**
	 * 向购物车中添加一条新的数据
	 * @param cart 购物车数据
	 * @throws InsertException 插入异常
	 */
	private void addNew(Cart cart) throws InsertException{
		try {
			
			Integer rows = cartMapper.addNew(cart);
			if(rows != 1){
				throw new InsertException("添加购物车异常！");
			}
			
		} catch (Exception e) {
			throw new InsertException("添加购物车异常ceshi！");
		}
	}
	
	/**
	 * 根据商品id数组删除购物车的数据
	 * @param ids 商品id的数组
	 * @return 成功影响的行数
	 */
	private void deleteByIds(Integer[] ids){
		Integer rows = cartMapper.deleteByIds(ids);
		if(rows != ids.length){
			throw new DeleteException("删除购物车数据失败！");
		}
	}
	
	/**
	 * 根据用户id和商品id得到购物车的数据
	 * @param uid 用户id
	 * @param gid 商品id
	 * @return 得到匹配的购物车数据
	 */
	private Cart findByUidAndGid(Integer uid,Long gid){
		return cartMapper.findByUidAndGid(uid, gid);
	}
	
	/**
	 * 修改购物车中的商品数量和修改人修改时间
	 * @param id 购物车id
	 * @param count 数量
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 */
	private void update(Integer id, Integer count, String modifiedUser, Date modifiedTime) throws UpdateException{

		Integer rows = cartMapper.update(id, count, modifiedUser, modifiedTime);
		if(rows != 1){
			throw new UpdateException("更新购物车数量异常");
		}
	}

	/**
	 * 查询用户的购物车列表
	 * @param uid 用户id
	 * @return 得到匹配的购物车列表数据
	 */
	private List<CartVO> findByUid(Integer uid){
		return cartMapper.findByUid(uid);
	}
	
	/**
	 * 根据购物车id查询用户的购物车
	 * @param ids 购物车id
	 * @return 得到匹配的购物车列表数据
	 */
	private Cart findById(Integer ids){
		return cartMapper.findById(ids);
	}
	
	/**
	 * 根据购物车主键cid数组得到购物车的数据
	 * @param cids 购物车主键cid的数组
	 * @return 得到购物车的数据
	 */
	private List<CartVO> findByIds(Integer[] cids){
		return cartMapper.findByIds(cids);
	}

}
