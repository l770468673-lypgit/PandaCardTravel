package com.xlu.po;

public class CookieInfo {
	/*Integer id;
	int member;
	int user;

	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}
*/
//	{"member":11400,"tjuser":1,"employee":1,"tjmember":11400}
	
	int _id;
	int member;
	int  tjuser;
	int  employee;
	int tjmember;
	public int getMember() {
		return member;
	}
	public void setMember(int member) {
		this.member = member;
	}
	public int getTjuser() {
		return tjuser;
	}
	public void setTjuser(int tjuser) {
		this.tjuser = tjuser;
	}
	public int getEmployee() {
		return employee;
	}
	public void setEmployee(int employee) {
		this.employee = employee;
	}
	public int getTjmember() {
		return tjmember;
	}
	public void setTjmember(int tjmember) {
		this.tjmember = tjmember;
	}

}
