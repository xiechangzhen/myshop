package com.linkage.myshop.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkage.myshop.entity.Cart;
import com.linkage.myshop.vo.CartVO;

/**
 * 购物车的持久层接口
 * @author Administrator
 *
 */
public interface CartMapper {
	
	/**
	 * 向购物车中添加一条新的数据
	 * @param cart 购物车数据
	 * @return 成功影响的行数
	 */
	Integer addNew(Cart cart);
	
	/**
	 * 根据购物车cid数组删除购物车的数据
	 * @param ids 购物车cid的数组
	 * @return 成功影响的行数
	 */
	Integer deleteByIds(Integer[] cids);

	/**
	 * 根据用户id和商品id得到购物车的数据
	 * @param uid 用户uid
	 * @param gid 商品gid
	 * @return 得到匹配的购物车数据
	 */
	Cart findByUidAndGid(@Param("uid") Integer uid,@Param("gid") Long gid);
	
	/**
	 * 查询用户的购物车列表
	 * @param uid 用户uid
	 * @return 得到匹配的购物车列表数据
	 */
	List<CartVO> findByUid(Integer uid);
	
	/**
	 * 根据购物车主键cid查询用户的购物车
	 * @param id 购物车主键cid
	 * @return 得到匹配的购物车列表数据
	 */
	Cart findById(Integer cid);
	
	
	/**
	 * 根据购物车主键cid数组得到购物车的数据
	 * @param ids 购物车主键cid的数组
	 * @return 得到购物车的数据
	 */
	List<CartVO> findByIds(Integer[] cids);
	
	/**
	 * 修改购物车中的商品数量和修改人修改时间
	 * @param id 购物车cid
	 * @param count 数量
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 * @return 成功影响的行数
	 */
	Integer update(@Param("id") Integer cid,@Param("count") Integer count,@Param("modifiedUser") String modifiedUser,@Param("modifiedTime") Date modifiedTime);
}
