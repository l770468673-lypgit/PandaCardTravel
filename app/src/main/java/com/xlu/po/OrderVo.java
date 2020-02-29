package com.xlu.po;

import java.util.List;

public class OrderVo {
	private String jym;
	private String tel;
	private String address;
	private String sfz;
	private Integer mid;
	private String name;
	private Float money;
	private Integer tjuser;
	private Integer tjmember;	
	private List<OrderDetail> list;

	public String getJym() {
		return jym;
	}

	public void setJym(String jym) {
		this.jym = jym;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSfz() {
		return sfz;
	}

	public void setSfz(String sfz) {
		this.sfz = sfz;
	}

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getTjuser() {
		return tjuser;
	}

	public void setTjuser(Integer tjuser) {
		this.tjuser = tjuser;
	}

	public Integer getTjmember() {
		return tjmember;
	}

	public void setTjmember(Integer tjmember) {
		this.tjmember = tjmember;
	}

	public List<OrderDetail> getList() {
		return list;
	}

	public void setList(List<OrderDetail> list) {
		this.list = list;
	}

	
}
