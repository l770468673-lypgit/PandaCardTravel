package com.xlu.po;

import java.io.Serializable;

public class ProInfo1 implements Serializable{
	 private int id; 
     private int coupon_max;
     public String getDesc_() {
		return desc_;
	}
	public void setDesc_(String desc_) {
		this.desc_ = desc_;
	}
	private double price;
     private String memo;
     private String name;
     private int score;
     private int active;
     private String pic;
     private int mid;
     private String type;
     private String desc_;
	private int pre_day;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCoupon_max() {
		return coupon_max;
	}
	public void setCoupon_max(int coupon_max) {
		this.coupon_max = coupon_max;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getPre_day() {
		return pre_day;
	}

	public void setPre_day(int pre_day) {
		this.pre_day = pre_day;
	}
}
