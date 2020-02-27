package com.example.coronahoxy.Route;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RouteData {
	public static final int TIME = 1111;
	public static final int ROUTE = 2222;

	public int type;
	public String description;
	public int commonTime;
	public Date date;
	public int order;
	public String monthDay;
	public String hourMin;

	private SimpleDateFormat monthFormat = new SimpleDateFormat("MM-dd");
	private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm");

	public RouteData(int type, String des, int common, int order, Date date){
		this.type = type;
		this.description = des;
		this.commonTime = common;
		this.order = order;
		this.date = date;
		monthDay = monthFormat.format(date);
		hourMin = hourFormat.format(date);
	}
}
