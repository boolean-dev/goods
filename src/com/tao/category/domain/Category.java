package com.tao.category.domain;

import java.util.List;

public class Category {
	
	private String cid;
	private String cname;
	private Category parent;
	private List<Category> childs;
	private String desc;
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public List<Category> getChilds() {
		return childs;
	}
	public void setChilds(List<Category> childs) {
		this.childs = childs;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
