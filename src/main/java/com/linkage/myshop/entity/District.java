package com.linkage.myshop.entity;

import java.io.Serializable;
/**
 * 
 * @author Administrator
 *
 */
public class District implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String parent;
	private String code;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "District [id=" + id + ", parent=" + parent + ", code=" + code + ", name=" + name + "]";
	}
	
}
