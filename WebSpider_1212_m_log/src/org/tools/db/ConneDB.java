package org.tools.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.tools.MD5;

public class ConneDB {

	/**
	 * 记录URL的IP地址信息
	 * 
	 */
	/*
	public void insertIpv6ByName(String url, String host, String ipv6str,
			String ipv4str, String agency, String date) {
		
		// 得到数据连接
		Connection conn = MysqlDBConn.getMysqlDBConn().getConnection();
		PreparedStatement psmt = null;
		// 拼写sql语句
		String sqlstr = "insert into ipv6info(URL,Host,Ipv6Addr,Ipv4Addr,Agency,Date) values(?,?,?,?,?,?)";
		try{
		psmt = conn.prepareStatement(sqlstr);
		psmt.setString(1, url);
		psmt.setString(2, host);
		psmt.setString(3, ipv6str);
		psmt.setString(4, ipv4str);
		psmt.setString(5, agency);
		psmt.setString(6, date);
		psmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
		    JDBCUtils.close(psmt, conn);
		}
	}
	*/
	//------------------------
	  public void insertIpv6ByName(String url, String host, String ipv6str,
			String ipv4str, String agency, String date) {
		
		// 得到数据连接
		Connection conn = MysqlDBConn.getMysqlDBConn().getConnection();
		PreparedStatement psmt = null;
		// 拼写sql语句
		String sqlstr = "insert  into ipv6info_test(url,host,ipv6addr,ipv4addr,agency,date) values(?,?,?,?,?,?)";
		try{
		psmt = conn.prepareStatement(sqlstr);
		//psmt.setString(1,uid);
		psmt.setString(1, url);
		psmt.setString(2, host);
		psmt.setString(3, ipv6str);
		psmt.setString(4, ipv4str);
		psmt.setString(5, agency);
		psmt.setString(6, date);
		psmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
		    JDBCUtils.close(psmt, conn);
		}
	}
  //--------------------------- 

	/**
	 * 记录URL的详细信息
	 *
	 */
	public void updateIpv6ByUrl(int status, String contype, String conlen,
			String server, String last, String url) {
		
		// 得到数据连接
		Connection conn = MysqlDBConn.getMysqlDBConn().getConnection();
		PreparedStatement psmt = null;
		// 拼写sql语句
		String sqlstr = "update ipv6info_test set StatusCode=?,ContentType=?,ContentLength=?,Server=?,LastModified=? where URL = ? ;";
		try{
		psmt = conn.prepareStatement(sqlstr);
		psmt.setInt(1, status);
		psmt.setString(2, contype);
		psmt.setString(3, conlen);
		psmt.setString(4, server);
		psmt.setString(5, last);
		psmt.setString(6, url);
		psmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
		    JDBCUtils.close(psmt, conn);
		}
	}

	/**
	 * 记录URL的标题信息
	 * 
	 * @param title
	 * @param url
	 */
	public void updateTitleByUrl(String title, String url) {
		
		// 得到数据连接
		Connection conn = MysqlDBConn.getMysqlDBConn().getConnection();
		PreparedStatement psmt = null;
		// 得到语句块
		String sqlstr = "update ipv6info_test set title = ? where url = ? ;";
		try{
		psmt = conn.prepareStatement(sqlstr);
		psmt.setString(1, title);
		psmt.setString(2, url);
		psmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
		    JDBCUtils.close(psmt, conn);
		}
	}

	/**
	 * 记录流媒体资源
	 * 
	 * @param url
	 */
	public void insertMMS(String url, String date) {
		String uid = MD5.getMD5(url);
		// 得到数据连接
		Connection conn = MysqlDBConn.getMysqlDBConn().getConnection();
		PreparedStatement psmt = null;
		// 拼写sql语句
		String sqlstr = "insert into mmsinfo_test(uid,url,date) values(?,?,?)";
		try {
			psmt = conn.prepareStatement(sqlstr);
			psmt.setString(1, uid);
			psmt.setString(2, url);
			psmt.setString(3, date);
			psmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(psmt, conn);
		}
	}

	/**
	 * 记录错误站点
	 * 
	 * @param url
	 * @param host
	 * @param remark
	 */
	public void insertError(String url, String host, int status, String remark,
			String date) {
		String uid = MD5.getMD5(url);
		// 得到数据连接
		Connection conn = MysqlDBConn.getMysqlDBConn().getConnection();
		PreparedStatement psmt = null;
		// 拼写sql语句
		String sqlstr = "insert into errorsite_test(uid,url,host,statuscode,remark,date) values(?,?,?,?,?,?)";
		try {
		psmt = conn.prepareStatement(sqlstr);
		psmt.setString(1, uid);
		psmt.setString(2, url);
		psmt.setString(3, host);
		psmt.setInt(4, status);
		psmt.setString(5, remark);
		psmt.setString(6, date);
		psmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    JDBCUtils.close(psmt, conn);
		}
	}

}
