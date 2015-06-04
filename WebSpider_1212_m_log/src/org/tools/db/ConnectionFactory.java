package org.tools.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection getConnection() {
    	String user = "root";
		String pwd = "1234";
		
		// 设定url
		String url = "jdbc:mysql://localhost:3306/franklin?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false";
        Connection con = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ce) {
            System.out.println(ce);
        }
        try {
            con = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException ce) {
            System.out.println(ce);
        }
        return con;
    }
}

