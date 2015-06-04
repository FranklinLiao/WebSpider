package org.net.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	public static Properties readProperty(String propertiesFileName) {
		PropertyUtil property = new PropertyUtil();
		InputStream in = property.getClass().getResourceAsStream(
				propertiesFileName);
		Properties prop = new Properties();
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
    /**
	 * 测试方法
	public static void main(String args[]) throws Exception {
		System.out.println(PropertyUtil.readProperty("/config/init.properties").getProperty("poolsize"));
	}
	*/
}
