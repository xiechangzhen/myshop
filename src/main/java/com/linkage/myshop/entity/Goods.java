package com.linkage.myshop.entity;
/**
 * 商品数据
 * @author Administrator
 *
 */
public class Goods extends BaseEntity {
	private static final long serialVersionUID = 299036652140395721L;
	
	private Long 	id;
	private Long 	categoryId;
	private String 	itemType;
	private String	title;
	private String 	sellPoint;
	private Integer price; //数据库中bingint
	private String  barcode;
	private String 	image;
	private Integer status;
	private Integer priority;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "Goods [id=" + id + ", categoryId=" + categoryId + ", itemType=" + itemType + ", title=" + title
				+ ", sellPoint=" + sellPoint + ", price=" + price + ", barcode=" + barcode + ", image=" + image
				+ ", status=" + status + ", priority=" + priority + "]";
	}

}
