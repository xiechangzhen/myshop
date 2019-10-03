package com.linkage.myshop.entity;

import java.util.Date;
/**
 * 订单数据
 * @author Administrator
 *
 */
public class Order extends BaseEntity{
	private static final long serialVersionUID = -1315387043489636524L;
	
	private Integer oid;
	private Integer uid;
	private String recvName;
	private String recvPhone;
	private String recvAddress;
	//数据库中为bigint
	private Integer totalPrice; 
	//'状态：0-未支付，1-已支付，2-已取
	private Integer status; 
	private Date orderTime;
	private Date payTime;
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getRecvName() {
		return recvName;
	}
	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}
	public String getRecvPhone() {
		return recvPhone;
	}
	public void setRecvPhone(String recvPhone) {
		this.recvPhone = recvPhone;
	}
	public String getRecvAddress() {
		return recvAddress;
	}
	public void setRecvAddress(String recvAddress) {
		this.recvAddress = recvAddress;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	@Override
	public String toString() {
		return "Order [oid=" + oid + ", uid=" + uid + ", recvName=" + recvName + ", recvPhone=" + recvPhone
				+ ", recvAddress=" + recvAddress + ", totalPrice=" + totalPrice + ", status=" + status + ", orderTime="
				+ orderTime + ", payTime=" + payTime + "]";
	}
	
}




	
