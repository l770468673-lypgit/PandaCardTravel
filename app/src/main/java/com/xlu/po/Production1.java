package com.xlu.po;

import java.io.Serializable;
import java.util.List;

public class Production1 implements Serializable{
	private MerInfo merchant;
	private ProInfo1 pro;
	private List<ProComment> comments;
	public List<ProComment> getComments() {
		return comments;
	}
	public void setComments(List<ProComment> comments) {
		this.comments = comments;
	}
	public MerInfo getMerchant() {
		return merchant;
	}
	public void setMerchant(MerInfo merchant) {
		this.merchant = merchant;
	}
	public ProInfo1 getPro() {
		return pro;
	}
	public void setPro(ProInfo1 pro) {
		this.pro = pro;
	}

}
