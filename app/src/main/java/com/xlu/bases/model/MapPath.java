package com.xlu.bases.model;

import android.graphics.Point;

import java.util.ArrayList;

public class MapPath {
	private ArrayList<Point> path = new ArrayList<Point>();
	private int width; 

	public MapPath(ArrayList<Point> path, int width) {
		super();
		this.path = path;
		this.width = width;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(ArrayList<Point> path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public ArrayList<Point> getPath() {
		return path;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	

}
