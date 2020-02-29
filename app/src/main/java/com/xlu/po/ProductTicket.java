package com.xlu.po;

import java.io.Serializable;

public class ProductTicket implements Serializable{
	private int active;
	private int coupon_max;
	private String desc_;
	private int id;
	private String luyin;
	private String memo;
	private int mid;
	private String name;
	private String pic;
	private double price;
	private double price_cost;
	private String price_fx1;
	private double price_other;
	private int score;
	private String tags;
	private int tuijian;
	private String type;

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getCoupon_max() {
		return coupon_max;
	}

	public void setCoupon_max(int coupon_max) {
		this.coupon_max = coupon_max;
	}

	public String getDesc_() {
		return desc_;
	}

	public void setDesc_(String desc_) {
		this.desc_ = desc_;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLuyin() {
		return luyin;
	}

	public void setLuyin(String luyin) {
		this.luyin = luyin;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice_cost() {
		return price_cost;
	}

	public void setPrice_cost(double price_cost) {
		this.price_cost = price_cost;
	}

	public String getPrice_fx1() {
		return price_fx1;
	}

	public void setPrice_fx1(String price_fx1) {
		this.price_fx1 = price_fx1;
	}

	public double getPrice_other() {
		return price_other;
	}

	public void setPrice_other(double price_other) {
		this.price_other = price_other;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getTuijian() {
		return tuijian;
	}

	public void setTuijian(int tuijian) {
		this.tuijian = tuijian;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
