package org.tools;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {

	// 获取日志记录器
	public static Logger logger = Logger.getLogger(Log.class.getName());

/*	public Log() {
		// 读取使用Java属性文件编写的配置文件
		PropertyConfigurator.configure("log4j.properties");
	}*/

   /* 
	public static void main(String[] args) {

		logger.debug("Log4jTest-->>debug");
		logger.info("Log4jTest-->>info");
		logger.warn("Log4jTest-->>warn");
		logger.error("Log4jTest-->>error");
	}
	*/
}
