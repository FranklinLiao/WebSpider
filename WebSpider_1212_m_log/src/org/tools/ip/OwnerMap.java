package org.tools.ip;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.net.util.Dates;
import org.tools.xml.GetContent;

public class OwnerMap {
	/**
	 * 根据XML文件生成IP地址所有机构Hash表
	 * 
	 * @return
	 */
	public static HashMap<String, String> generateHash() {
		String xmlpath = "ipv6blocks("+Dates.getDate()+").xml";
		//String xmlpath = "ipv6blocks(2012-04-01).xml";
		SAXBuilder builder = new SAXBuilder(false);
		HashMap<String, String> owners = new HashMap<String, String>();
		
		try {
			Document doc = builder.build(xmlpath);
			Element ipv6blocks = doc.getRootElement();
			List blocklist = ipv6blocks.getChildren("ipv6block");
			for (Iterator iter = blocklist.iterator(); iter.hasNext();) {
				Element ipv6block = (Element) iter.next();
				String block = ipv6block.getChildTextTrim("block");
				String owner = ipv6block.getChildTextTrim("descr");
				String[] splitStr = GetContent.split(block, "::/");
				owners.put(
						Ipv6Convert.getPrefix(splitStr[0],
								Integer.parseInt(splitStr[1])), owner);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return owners;
	}

	/**
	 * 根据IP地址前缀查询所有机构名称
	 * 
	 * @param prefix
	 * @return
	 */
	public  static String getOwner(String prefix) {
		return generateHash().get(prefix);
	}

//	public static void main(String args[])  {
//		System.out.println(getOwner("00100100000000011001101000000000"));
//	}
	
}
