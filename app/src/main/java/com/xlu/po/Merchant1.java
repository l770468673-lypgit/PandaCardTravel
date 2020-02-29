package com.xlu.po;

import java.io.Serializable;

public class Merchant1 implements Serializable{
	float price_market;
	int id;
	int commAmount;
	Float satisfaction;
	float price;
	String name;
	String pic;
	String weidu;
	float juli;
	String jindu;
	String tags;

	public float getPrice_market() {
		return price_market;
	}
	public void setPrice_market(float price_market) {
		this.price_market = price_market;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommAmount() {
		return commAmount;
	}
	public void setCommAmount(int commAmount) {
		this.commAmount = commAmount;
	}
	public Float getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(Float satisfaction) {
		this.satisfaction = satisfaction;
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
	public String getWeidu() {
		return weidu;
	}
	public void setWeidu(String weidu) {
		this.weidu = weidu;
	}
	public float getJuli() {
		return juli;
	}
	public void setJuli(float juli) {
		this.juli = juli;
	}
	public String getJindu() {
		return jindu;
	}
	public void setJindu(String jindu) {
		this.jindu = jindu;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}
