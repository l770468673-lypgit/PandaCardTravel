package com.xlu.po;

import java.io.Serializable;

public class ProductSpecal implements Serializable{
   private int id;
    private double price; 
    private String name;
    private int score;
    private String pic;
    private int mid;
    private int price_other;
	private int coupon_max;
    private int count;
    private String desc_;
    private boolean isChose=true;
    private boolean isUseDiscount=false;
    private double weidu;
    private double jindu;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
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
	public int getPrice_other() {
		return price_other;
	}
	public void setPrice_other(int price_other) {
		this.price_other = price_other;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDesc_() {
		return desc_;
	}
	public void setDesc_(String desc_) {
		this.desc_ = desc_;
	}
	public boolean getIsChose() {
		return isChose;
	}
	public void setIsChose(boolean isChose) {
		this.isChose = isChose;
	}
	public double getWeidu() {
		return weidu;
	}
	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}
	public double getJindu() {
		return jindu;
	}
	public void setJindu(double jindu) {
		this.jindu = jindu;
	}

	public int getCoupon_max() {
		return coupon_max;
	}

	public void setCoupon_max(int coupon_max) {
		this.coupon_max = coupon_max;
	}

    public boolean isUseDiscount() {
        return isUseDiscount;
    }

    public void setUseDiscount(boolean useDiscount) {
        isUseDiscount = useDiscount;
    }
}
