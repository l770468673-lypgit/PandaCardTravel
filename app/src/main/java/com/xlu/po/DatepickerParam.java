package com.xlu.po;

import java.io.Serializable;
import java.util.Calendar;

public class DatepickerParam implements Serializable {
	public Calendar selectedDay = null;
	public Calendar startDate = null;
	//
	public int dateRange = 0;
	public String title = "出发日期";
}
