package org.tools.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.tools.Log;


import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;


/**
 * 提取网页上的最新IPv6地址块信息，并生成XML文件
 * @author JWX
 *
 */
public class TableContentByHtmlParse {

	public Element getTableContent(Element element, int rows, int cols) {
		Element resultElement = null;
		List<Element> trList = element.getAllElements(HTMLElementName.TR);
		if (rows < trList.size()) {
			Element trElement = trList.get(rows);
			List<Element> tdList = trElement.getAllElements(HTMLElementName.TD);
			if (cols < tdList.size()) {
				// Element tdElement=tdList.get(cols);
				resultElement = tdList.get(cols);
			}
		}
		return resultElement;
	}

	public static void getIpList(String url) {
		Source source = null;
		try {
			source = new Source(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Element> tableList = source
				.getAllElements(HTMLElementName.TABLE);
		Element element = tableList.get(3);
		List<Element> trList = element.getAllElements(HTMLElementName.TR); 
		Log.logger.info("开始更新IPv6地址块最新配置文件...");
		//System.out.println("开始更新IPv6地址块最新配置文件...");
		for (int i = 1; i < trList.size(); i++) {
			Element ipBlock = new TableContentByHtmlParse()
					.getTableContent(element, i, 1);
			Element num = new TableContentByHtmlParse()
					.getTableContent(element, i, 2);
			if (ipBlock != null && num != null) {
				String ipStr = ipBlock.getContent().toString();
				//----------------
				//System.out.println(ipStr);
				//--------------------
				String ipBlockStr = ipStr.substring(ipStr.indexOf('>')+1, ipStr.lastIndexOf('<'));
				String descr = GetContent.getContentUsingStringExtractor(
						"http://wq.apnic.net/apnic-bin/whois.pl?searchtext="+ipBlockStr,
						false);
				try {
					new XMLUtil().ModifyXML(Integer.toString(i), ipBlockStr, num.getContent().toString(),
							descr);
					Log.logger.info(i +" completed");
					//System.out.println(i +" completed");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		Log.logger.info("IPv6地址块最新配置文件更新完成！");
		//System.out.println("IPv6地址块最新配置文件更新完成！");
	}
   
	
	public static void main(String[] args) {
		getIpList("http://ipwhois.cnnic.cn/ipstats/detail.php?obj=ipv6&country=CN");
		
	}
   
}