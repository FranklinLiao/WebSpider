package org.tools.xml;

import java.util.ArrayList;
import org.htmlparser.parserapplications.StringExtractor;
import org.htmlparser.util.ParserException;

/**
 * htmlparser提取网页正文
 */
public class GetContent {

	public static String getContentUsingStringExtractor(String url, boolean link) {
		StringExtractor se = new StringExtractor(url);
		String text = null;
		String content = null;
		try {
			text = se.extractStrings(link);
			String[] splitStr = split(text, "descr:");
			content = splitStr[1].replace("＆", "&amp;").replace("，", ",").replace("amp;", "").trim();
			if (content.indexOf("country:") != -1)
				content = content.substring(0, content.indexOf("country:"));
			if (content.indexOf("admin-c:") != -1)
				content = content.substring(0, content.indexOf("admin-c:"));						
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static String[] split(String str, String splitsign) {
		int index;
		if (str == null || splitsign == null)
			return null;
		ArrayList al = new ArrayList();
		while ((index = str.indexOf(splitsign)) != -1) {
			al.add(str.substring(0, index));
			str = str.substring(index + splitsign.length());
		}
		al.add(str);
		return (String[]) al.toArray(new String[0]);
	}

   
   /* 
	public static void main(String[] args) {
		GetContent g = new GetContent();
		System.out
				.println(g
						.getContentUsingStringExtractor(
								"http://wq.apnic.net/apnic-bin/whois.pl?searchtext=2405:e600::/32",
								//"http://wq.apnic.net/apnic-bin/whois.pl?searchtext=" + "2405:e600::/32" + "&amp;object_type=inet6num",
								false));
	}
	*/
	
	
}
