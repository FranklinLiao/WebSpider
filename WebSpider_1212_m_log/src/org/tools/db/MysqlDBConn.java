package org.tools.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.net.util.PropertyUtil;

public class MysqlDBConn {
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	private static String DBUrl = "";
	private static String DBUser = "root";
	private static String DBPassword = "1234";
	private static Properties prop = PropertyUtil.readProperty("/config/db.properties");
	private static MysqlDBConn mysqldbconn = null;
	
	Connection conn = null;
	static {
		try {
			Class.forName(DBDRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("驱动加载失败", e);
		}
	}

	public static MysqlDBConn getMysqlDBConn() {
		if (mysqldbconn == null) {
			mysqldbconn = new MysqlDBConn();
		}
		return mysqldbconn;
	}

	public Connection getConnection() {
		try {
			DBUrl = "jdbc:mysql://"+prop.getProperty("db_host")+":3306/"+prop.getProperty("db_name")+"?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false";
			DBUser = prop.getProperty("db_user");
			DBPassword = prop.getProperty("db_psd");
			conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("连接数据库失败", e);
		}
		return conn;
	}

}