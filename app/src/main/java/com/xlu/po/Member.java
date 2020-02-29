package com.xlu.po;

import java.io.Serializable;

public class Member implements Serializable {
	private int id;
	private double rmoney;
	private String sex;
	private String pwd;
	private String area;
	private String tel;
	private String name;
	private String birthday;
	private String jym;
	private String active;
	private String type;
	private String imgurl;
	private String qianming;
	private int isguanzhu;
	private int jifen;
	private int isvip;
	private int discountquan;
	private String card_no;

	public int getIsvip() {
		return isvip;
	}

	public void setIsvip(int isvip) {
		this.isvip = isvip;
	}

	public int getDiscountquan() {
		return discountquan;
	}

	public void setDiscountquan(int discountquan) {
		this.discountquan = discountquan;
	}

	public String getJym() {
		return jym;
	}

	public int getIsguanzhu() {
		return isguanzhu;
	}

	public void setIsguanzhu(int isguanzhu) {
		this.isguanzhu = isguanzhu;
	}

	public void setJym(String jym) {
		this.jym = jym;
	}

	public int getJifen() {
		return jifen;
	}

	public void setJifen(int jifen) {
		this.jifen = jifen;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getRmoney() {
		return rmoney;
	}

	public void setRmoney(double rmoney) {
		this.rmoney = rmoney;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public void setQianming(String qianming) {
		this.qianming = qianming;
	}

	public String getQianming() {
		return qianming;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }
}
