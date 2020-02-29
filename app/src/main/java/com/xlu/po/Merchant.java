package com.xlu.po;

import java.io.Serializable;

public class Merchant implements Serializable{
	float price_afterdis;
	int id;
	int commAmount;
	float satisfaction;
	int price_predis;
	String name;
	int price_dec;
	String pic;
	public float getPrice_afterdis() {
		return price_afterdis;
	}
	public void setPrice_afterdis(float price_afterdis) {
		this.price_afterdis = price_afterdis;
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
	public float getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(float satisfaction) {
		this.satisfaction = satisfaction;
	}
	public int getPrice_predis() {
		return price_predis;
	}
	public void setPrice_predis(int price_predis) {
		this.price_predis = price_predis;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice_dec() {
		return price_dec;
	}
	public void setPrice_dec(int price_dec) {
		this.price_dec = price_dec;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	

}
