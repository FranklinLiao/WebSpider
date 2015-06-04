package org.net.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tools.GenerateXML;
import org.tools.ip.Ipv6Block;

public class BlockMatch {
	
	public static void match() {
		GenerateXML gx = new GenerateXML();
		String xmlpath = "ipv6blocks.xml";
		SAXBuilder builder = new SAXBuilder(false);
		List<String> blocks = new ArrayList<String>();
		Set<String> ts = ReadAddr.readAddr("http://bgp.potaroo.net/v6/as6447/bgptable.txt");	
	    Map<String,Map<String, String>> fs = FilterFactory.getFilters(ts);
	    FileWriter fw = null;
	    BufferedWriter bw = null;
	    
		try {
			File file = new File("中国IPv6地址块匹配路由表结果.txt");   
            if(!file.exists()){   
                file.createNewFile();   
            }   
            fw = new FileWriter(file);   
            bw = new BufferedWriter(fw); 
            
			Document doc = builder.build(xmlpath);
			Element ipv6blocks = doc.getRootElement();
			List blocklist = ipv6blocks.getChildren("ipv6block");
			for (Iterator iter = blocklist.iterator(); iter.hasNext();) {
				Element ipv6block = (Element) iter.next();
				String block = ipv6block.getChildTextTrim("block");
				blocks.add(block);
			}
			for (Iterator<String> iter = blocks.iterator(); iter.hasNext();) {
				String bl = iter.next();
				Ipv6Block ipb = new Ipv6Block(bl);	
				Map<String, String> tbs = fs.get(ipb.getPreFix());				
				Set<String> res = new HashSet<String>();
				int count = 0;
				
				if(tbs!=null)
				{
				   for(String tb : tbs.keySet())
				   {			
					     if(tb.startsWith(ipb.getFixStr()))
					     {
						    count++;
						    res.add(tbs.get(tb));
					     }
				    }
				}
				
		        bw.write("中国IPv6地址块："+bl+" 共匹配"+count+"条路由记录，匹配路由地址块为："+res);   
		        bw.newLine();
			}							
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bw!=null)
			{
				try {
					bw.flush();
					bw.close();   
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fw!=null)
			{
				try {
					fw.close(); 	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		        
		}
	}
	
	public static void main(String args[]) {
		match();
	}
}