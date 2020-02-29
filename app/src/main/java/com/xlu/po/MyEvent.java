package com.xlu.po;

public class MyEvent {
	private int id;
	private Object obj;

	public MyEvent(int id) {
		this.id = id;
	}

	public MyEvent(int id, Object obj) {
		this.id = id;
		this.obj = obj;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "MyEvent [id=" + id + ", obj=" + obj + "]";
	}

}
