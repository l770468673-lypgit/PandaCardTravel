package com.xlu.po;

public class Comment {
	int id;
	String name;
	String imgurl;
	String desc_;
	String create_date;
	
	
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getDesc_() {
		return desc_;
	}
	public void setDesc_(String desc) {
		desc_ = desc;
	}
	public int getCreate_user() {
		return create_user;
	}
	public void setCreate_user(int createUser) {
		create_user = createUser;
	}
	int create_user;
}
