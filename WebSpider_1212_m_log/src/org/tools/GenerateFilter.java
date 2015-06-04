package org.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;

/**
 * 产生URL排重用的过滤器
 * @author JWX
 *
 */
public class GenerateFilter {
	private static HashSet<String> filter = null;
	
	static {
		setFilter();
	}
	
	@SuppressWarnings("unchecked")
	private static void setFilter() {
		File dirFile = new File("filter.obj");
		if(!dirFile.exists())
		{
			filter = new HashSet<String>();
		}
		else
		{
			// 反序列化对象
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream("filter.obj"));
				filter = (HashSet<String>)in.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	public static HashSet<String> getFilter() {
		return filter;
	}
}