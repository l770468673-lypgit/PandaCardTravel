package com.xlu.po;

public class FreeTime {
	public FreeTime() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FreeTime(Integer id, Integer times) {
		super();
		this.id = id;
		this.times = times;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}

	private Integer id;
	private Integer times;
}
