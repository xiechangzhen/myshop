package com.linkage.myshop.entity;
/**
 * 订单商品数据
 * @author Administrator
 *
 */
public class OrderItem extends BaseEntity {

	private static final long serialVersionUID = -230337979227737558L;
	
	private Integer id;
	private Integer oid;
	private Long gid;
	private String goodsTitle;
	private String goodsImage;
	private Integer goodsPrice;
	private Integer goodsNum;
	public Integer getId() {
		return id;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public Integer getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Integer goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Integer getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	public Long getGid() {
		return gid;
	}
	public void setGid(Long gid) {
		this.gid = gid;
	}
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", oid=" + oid + ", gid=" + gid + ", goodsTitle=" + goodsTitle + ", goodsImage="
				+ goodsImage + ", goodsPrice=" + goodsPrice + ", goodsNum=" + goodsNum + "]";
	}
	
}
