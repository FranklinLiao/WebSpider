package org.spider.SiteSpider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.tools.MD5;
import org.tools.db.ConneDB;

/**
 * 辅助判断网页链接地址是否有效的类: 主要判断 1.是否为特定格式的文件,将剔除非不接受的文档类型 2.链接的深度是否过长
 * 
 * @author f705 2011.5
 */

public class IsValidate {
	private boolean isValidate = false;

	public static boolean isValidate(String url, int deepth) {
		IsValidate iv = new IsValidate();
		iv.validate(url, deepth);
		return iv.isValidate;
	}

	public void validate(String urlString, int deepth) {
	//	String uid = "";
		int deep = deepth;
		if (urlString.startsWith("http:")) {
			if (!(urlString.endsWith(".doc") || urlString.endsWith(".xls")
					|| urlString.endsWith(".ppt") || urlString.endsWith(".pdf")
					|| urlString.endsWith(".rar") || urlString.endsWith(".zip")
					|| urlString.endsWith(".exe") || urlString.endsWith(".rm")
					|| urlString.endsWith(".rmvb")
					|| urlString.endsWith(".rpm") || urlString.endsWith(".avi")
					// ||urlString.endsWith(".mp3")
					|| urlString.endsWith(".wmv") || urlString.endsWith(".gz")
					|| urlString.endsWith(".bz2") || urlString.endsWith(".iso")
			// ||urlString.endsWith(".asf")
			|| urlString.endsWith(".swf"))) {
				if(urlString.contains("mirrors")){
					isValidate = false;
				}
				else if (urlString.split("/").length < (deep + 2)) {
					isValidate = true;
				}
			}
		} else if (urlString.startsWith("mms:")) {
			// 记录URL到数据库
			ConneDB td = new ConneDB();
			Date date = new Date();// 获得系统时间.
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(date);
			//uid = MD5.getMD5(urlString);
			td.insertMMS(urlString, nowTime);
		}
	}

}
