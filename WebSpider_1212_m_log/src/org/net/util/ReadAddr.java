package org.net.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ReadAddr {
	public static Set<String> readAddr(String strUrl) {    
        BufferedReader br = null;
        Set<String> hs = new LinkedHashSet<String>();
		try {
			 URL url = new URL(strUrl);
			 URLConnection uc = url.openConnection();
        	 br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        	 String str = "";
        	 while((str = br.readLine())!=null) {
        		 hs.add(str.substring(0,str.indexOf("/")+4));   		 
        	 }	
		} catch (MalformedURLException e) {
		     e.printStackTrace();
		} catch (IOException e) {
		     e.printStackTrace();
		} finally {
			if(null != br)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return hs;
		}
	}
        	 
        
	public static void writeAddr(Set<String> ts) throws IOException {
		FileWriter fw = new FileWriter("bgptable.txt");
		int i = 0;
		for(Iterator<String> it = ts.iterator(); it.hasNext(); ) { 
			fw.write((++i)+"\t"+it.next()+"\r\n");
		}
		fw.close();
	}
	
	public static void main(String args[]) throws IOException {
		Set<String> ts = readAddr("http://bgp.potaroo.net/v6/as6447/bgptable.txt");	
		writeAddr(ts);
	}
}
