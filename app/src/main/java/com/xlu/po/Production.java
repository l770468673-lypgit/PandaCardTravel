package com.xlu.po;

import java.io.Serializable;
import java.util.List;

public class Production implements Serializable{
	private MerInfo merchant;
	private List<ProInfo1> pros;
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
	public List<ProInfo1> getPros() {
		return pros;
	}
	public void setPros(List<ProInfo1> pros) {
		this.pros = pros;
	}

}
