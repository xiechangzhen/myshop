package com.linkage.myshop.vo;

import java.io.Serializable;
import java.util.List;

import com.linkage.myshop.entity.OrderItem;
/**
 * 订单详情数据
 * @author Administrator
 *
 */
public class OrderVO implements Serializable{

	private static final long serialVersionUID = 4174334308700388730L;
	private Integer id;
	private Integer uid;
	private String recvName;
	private String recvPhone;
	private String recvAddress;
	private Integer totalPrice;
	private Integer status;
	private List<OrderItem> items;
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
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "OrderVO [id=" + id + ", uid=" + uid + ", recvName=" + recvName + ", recvPhone=" + recvPhone
				+ ", recvDistrict="  + ", recvAddress=" + recvAddress + ", totalPrice=" + totalPrice
				+ ", status=" + status + ", items=" + items + "]";
	}
	
}
