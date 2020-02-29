package com.xlu.po;

import java.io.Serializable;

public class Help implements Serializable {
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	String tmoney;
	String create_date;
	String name;
	String imgurl;
	String title;
	String city;

	int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTmoney() {
		return tmoney;
	}

	public void setTmoney(String tmoney) {
		this.tmoney = tmoney;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCreate_user() {
		return create_user;
	}

	public void setCreate_user(int create_user) {
		this.create_user = create_user;
	}

	int create_user;

	@Override
	public String toString() {
		return "Help [id=" + id + ", tmoney=" + tmoney + ", create_date="
				+ create_date + ", name=" + name + ", imgurl=" + imgurl
				+ ", title=" + title + ", city=" + city + ", status=" + status
				+ ", create_user=" + create_user + "]";
	}
	
	
}
