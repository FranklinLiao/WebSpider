package org.tools;

import java.util.HashMap;
import java.util.Iterator;

import org.tools.ip.OwnerMap;

public class GenerateMap {
	private static SimpleBloomFilter filter;
	private static HashMap<String, String> hm;
	
	private GenerateMap(){
	}
	
	static{
		filter = new SimpleBloomFilter();
		hm = OwnerMap.generateHash();
		Iterator it = hm.keySet().iterator();
		while (it.hasNext()) {
			String key;
			key = (String) it.next();
			filter.add(key);
		}
	}

	public static SimpleBloomFilter getFilter() {
		return filter;
	}

	public static HashMap<String, String> getHm() {
		return hm;
	}
	
}
