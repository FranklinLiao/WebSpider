package org.tools.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.tools.Log;

public class IPv6Extractor {

	// 从一个URL下载HTML文本到一个本地字符串
	private static String getDocumentAt(String urlString) {
		// 本地字符串缓冲区
		StringBuffer html_text = new StringBuffer();
		try {
			// 根据urlString创建一个指向Web的网络资源
			URL url = new URL(urlString);
			// 创建一个到该网络资源的连接
			URLConnection conn = url.openConnection();
			// 创建输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			// 逐行读取文本放到缓冲区
			while ((line = reader.readLine()) != null)
				html_text.append(line + "\n");
			reader.close();
		} catch (MalformedURLException e) {
			System.out.println("无效的URL: " + urlString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html_text.toString();
	}

	// 从一个字符串提取信息
	public static void extractUrl(String url) throws IOException {
		try {
			// 获得网页文本内容
			String str = IPv6Extractor.getDocumentAt(url);
			
			// 创建IPv6地址块信息的正则表达式
			Pattern content = Pattern
					.compile("searchtext=(.*)&amp;");
			
			// 提取地址块信息
			Matcher mc = content.matcher(str);
			int count = 0;
			while (mc.find()) {
				//获得地址序号
				count++;
				//获得地址块信息
				String ipBlockStr = mc.group(1);
				//获得地址块大小信息
				String blockSize = ipBlockStr.substring(ipBlockStr.indexOf("/"));			
				//获得地址块所属机构信息
				String descr = GetContent.getContentUsingStringExtractor(
						"http://wq.apnic.net/apnic-bin/whois.pl?searchtext="+ipBlockStr+"&amp;object_type=inet6num",
						false);
				try {
					new XMLUtil().ModifyXML(Integer.toString(count), ipBlockStr, blockSize, descr);
					Log.logger.info(count +" completed");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}				
		} catch (PatternSyntaxException e) {
			System.out.println("正则表达式语法错误");
		} catch (IllegalStateException e) {
			System.out.println("找不到匹配字符串");
		}
	}

	public static void getIpList(String url) {
		Log.logger.info("开始更新IPv6地址块最新配置文件...");
		try {
			IPv6Extractor.extractUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Log.logger.info("IPv6地址块最新配置文件更新完成！");
	}
	
	public static void main(String[] args) {
		try {
			IPv6Extractor
					.extractUrl("http://www-public.int-evry.fr/~maigron/RIR_Stats/RIR_Delegations/Delegations/IPv6/CN.html#APNIC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}