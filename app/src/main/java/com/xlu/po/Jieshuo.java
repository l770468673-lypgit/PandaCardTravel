package com.xlu.po;

public class Jieshuo {

	private Integer id;
	private Integer jid;
	private String name;
	private String memo;
	private String mac;
	private String pic;
	private String yuyin;
	private double latitude;
	private double longitude;
	private int zuobiao_x;
	private boolean isListen = false;
	private int zuobiao_y;
	private int active;
	private Integer quanju;
	private String desc_;
	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean selected) {
		isSelected = selected;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getJid() {
		return jid;
	}
	public void setJid(Integer jid) {
		this.jid = jid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getYuyin() {
		return yuyin;
	}
	public void setYuyin(String yuyin) {
		this.yuyin = yuyin;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getZuobiao_x() {
		return zuobiao_x;
	}
	public void setZuobiao_x(int zuobiao_x) {
		this.zuobiao_x = zuobiao_x;
	}
	public boolean isListen() {
		return isListen;
	}
	public void setListen(boolean isListen) {
		this.isListen = isListen;
	}
	public int getZuobiao_y() {
		return zuobiao_y;
	}
	public void setZuobiao_y(int zuobiao_y) {
		this.zuobiao_y = zuobiao_y;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public Integer getQuanju() {
		return quanju;
	}
	public void setQuanju(Integer quanju) {
		this.quanju = quanju;
	}
	public String getDesc_() {
		return desc_;
	}
	public void setDesc_(String desc_) {
		this.desc_ = desc_;
	}



}
