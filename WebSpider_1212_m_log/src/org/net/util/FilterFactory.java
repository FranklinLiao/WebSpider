package org.net.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.tools.ip.Ipv6Block;

public class FilterFactory {

	public static Map<String,Map<String, String>> getFilters(Set<String> hs) {
		Map<String,Map<String, String>> fm = new HashMap<String,Map<String, String>>();
		fm.put("2001", getFilter(hs, "2001"));
		fm.put("2400", getFilter(hs, "2400"));
		fm.put("2401", getFilter(hs, "2401"));
		fm.put("2402", getFilter(hs, "2402"));
		fm.put("2403", getFilter(hs, "2403"));
		fm.put("2404", getFilter(hs, "2404"));
		fm.put("2405", getFilter(hs, "2405"));
		fm.put("2406", getFilter(hs, "2406"));
		fm.put("2407", getFilter(hs, "2407"));
		fm.put("2408", getFilter(hs, "2408"));
		fm.put("2409", getFilter(hs, "2409"));
		fm.put("240c", getFilter(hs, "240c"));
		fm.put("240e", getFilter(hs, "240e"));
		return fm;
	}
	
	public static Map<String, String> getFilter(Set<String> hs, String prefix) {
		Map<String, String> filter = new HashMap<String, String>();
		for (Iterator<String> iter = hs.iterator(); iter.hasNext();) {
			 String block = iter.next().trim();
		     Ipv6Block ipb = new Ipv6Block(block);
		    
	         if(block.startsWith(prefix))
	         {
	    	    filter.put(ipb.getFixStr(), block);
	         }
		}
		return filter;
	}
}
