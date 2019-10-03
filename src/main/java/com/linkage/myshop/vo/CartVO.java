package com.linkage.myshop.vo;

import java.io.Serializable;
/**
 * 购物车显示VO类
 * @author Administrator
 *
 */
public class CartVO implements Serializable{

	private static final long serialVersionUID = 1968311535406154847L;
	
	private Integer id;
	private Integer uid;
	private Long gid;
	private String image;
	private String title;
	private Integer count;
	private Integer oldPrice;
	private Integer newPrice;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Long getGid() {
		return gid;
	}
	public void setGid(Long gid) {
		this.gid = gid;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(Integer oldPrice) {
		this.oldPrice = oldPrice;
	}
	public Integer getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(Integer newPrice) {
		this.newPrice = newPrice;
	}
	@Override
	public String toString() {
		return "CartVO [id=" + id + ", uid=" + uid + ", gid=" + gid + ", image=" + image + ", title=" + title
				+ ", count=" + count + ", oldPrice=" + oldPrice + ", newPrice=" + newPrice + "]";
	}
	
}
