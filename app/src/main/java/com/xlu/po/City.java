package com.xlu.po;

public class City {

	private String id;
	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "City{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", memo='" + memo + '\'' +
				", bianhao='" + bianhao + '\'' +
				", zimu='" + zimu + '\'' +
				'}';
	}

	private String name;
	private String memo;
	private String bianhao;
	private String zimu;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public City(String id, String name, String memo, String bianhao, String zimu) {
		super();
		this.id = id;
		this.name = name;
		this.memo = memo;
		this.bianhao = bianhao;
		this.zimu = zimu;
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

	public String getBianhao() {
		return bianhao;
	}

	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}

	public String getZimu() {
		return zimu;
	}

	public void setZimu(String zimu) {
		this.zimu = zimu;
	}

}
