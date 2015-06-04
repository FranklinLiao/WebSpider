package org.spider.SiteSpider;

/**
 * 网页资源分析器:
 * 分析当前网页上的资源类型等信息
 * 可以分析资源类型、大小、服务器软件版本、网页标题等信息
 * 并保存到数据库中
 * @author jwx 2011.5.10
 **/

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.tools.Log;
import org.tools.MD5;
import org.tools.db.ConneDB;
import org.tools.ip.IsIpv6;

public class GetHTML {
	//是否是正常链接
	private boolean isWell = false;
	//是否是网页链接
	private boolean isWeb = false;

	public boolean isWell() {
		return isWell;
	}
	
	public boolean isWeb() {
		return isWeb;
	}
	
	public void getContent(String url) {
		// HTTP响应消息头字段
		String hType = "";
		String hLength = "";
		String hServer = "";
		String hLast = "";
		//String uid1 = "";
		//String uid2 = "";
		ConneDB td = new ConneDB();
		HttpClient httpClient = new HttpClient();

		// 设置参数：超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);   //设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);           //设置读数据超时时间(单位毫秒) 
		
		GetMethod getMethod = null;
		int statusCode = 0;
		try {
			// 创建HTTP Get请求
			getMethod = new GetMethod(url);
			getMethod.setFollowRedirects(false);
			getMethod.addRequestHeader("User-Agent",
					"Mozilla/4.0 (compatible;MSIE 7.0;Windows NT 5.1;)");
			getMethod.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");

			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());

			//获取状态码
			statusCode = httpClient.executeMethod(getMethod);

			URL strURL = new URL(url);

			if (statusCode != HttpStatus.SC_OK) {
				// System.err.println("Method failed: "+
				// getMethod.getStatusLine() + " for url " + url);
				Log.logger.error("Method failed: " + getMethod.getStatusLine()
						+ " for url " + url);
				//String uid1 = MD5.getMD5(url);
				td.insertError(url, strURL.getHost(), statusCode, getMethod
						.getStatusLine().toString(), new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(new Date()));
			} else {
				isWell = true;
			
				if (getMethod.getResponseHeader("Content-Type") != null) {
					hType = getMethod.getResponseHeader("Content-Type")
							.getValue();
					if(!"".equals(hType) && (hType.contains("text/html")||hType.contains("text/plain")||hType.contains("text/xml")))
						isWeb = true;
				}

				if (getMethod.getResponseHeader("Content-Length") != null) {
					hLength = getMethod.getResponseHeader("Content-Length")
							.getValue();
				}

				if (getMethod.getResponseHeader("Server") != null) {
					hServer = getMethod.getResponseHeader("Server").getValue();
				}

				if (getMethod.getResponseHeader("Last-Modified") != null) {
					hLast = getMethod.getResponseHeader("Last-Modified")
							.getValue();
				}
				td.updateIpv6ByUrl(statusCode, hType, hLength, hServer, hLast,
						url);
			}
		} catch (Exception e) {
				//uid2 = MD5.getMD5(url);
				td.insertError(url, IsIpv6.GetDomainName(url), statusCode,
						e.toString(), new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(new Date()));
		} finally {
			// 释放连接
			if (getMethod != null) {
				getMethod.releaseConnection();
				
			}
		}
	}

	// 补全URL相对路径
	public static String linkComplete(String url, String backLink) {
		// 判断是否为相对路径
		if (url.indexOf("http") == -1) {
			url = backLink.concat(url);
		}

		if (url.indexOf("#") >= 0) {
			url = url.substring(0, url.indexOf("#"));
		}
		return url;
	}

	/**
	 * 取得页面编码
	 * 
	 * @param url
	 * @return
	 */
	public String getCharset(String url) throws Exception {
		String charset = "";
		HttpURLConnection httpurlcon = null;

		URL httpurl = new URL(url);

		httpurlcon = (HttpURLConnection) httpurl.openConnection();
		httpurlcon.setRequestProperty("User-agent", "Mozilla/4.0");
		charset = httpurlcon.getContentType();
		httpurlcon.disconnect();
		// 如果可以找到
		if (charset.indexOf("charset=") != -1) {
			charset = charset.substring(charset.indexOf("charset=")
					+ "charset=".length(), charset.length());
			charset = charset.toLowerCase();
		} else
			charset = "utf-8";
		return charset;
	}
}