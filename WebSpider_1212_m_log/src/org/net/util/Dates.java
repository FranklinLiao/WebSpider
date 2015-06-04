package org.net.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {
	/*
	 * 获取当前日期
	 */
	public static String getDate() {
	    Date date = new Date();// 获得系统时间.
	    String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
	    return nowTime;
	}
}
