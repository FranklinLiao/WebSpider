package org.spider.SiteSpider;

import java.util.*;
import java.sql.*;

import org.apache.commons.logging.LogFactory;
import org.tools.GenerateFilter;
import org.tools.IsChina;
import org.tools.Log;
import org.tools.MD5;
import org.tools.db.MysqlDBConn;

/**
 * This class uses a JDBC database to store a spider workload.
 * 
 */
public class SpiderSQLWorkload implements IWorkloadStorable {

	/**
	 * The JDBC connection.
	 */
	Connection connection;

	/**
	 * A prepared SQL statement to clear the workload.
	 */
	PreparedStatement prepClear;

	/**
	 * A prepared SQL statement to assign a workload.
	 */
	PreparedStatement prepAssign;
	
	/**
	 * A prepared SQL statement to resume a workload.
	 */
	PreparedStatement resumeAssign;

	/**
	 * A prepared SQL statement to get the status of a URL.
	 */
	PreparedStatement prepGetStatus;

	/**
	 * A prepared SQL statement to set the status.
	 */
	PreparedStatement prepSetStatus1;

	/**
	 * A prepared SQL statement to set the status.
	 */
	PreparedStatement prepSetStatus2;

	/**
	 * A prepared SQL statement to set the status.
	 */
	PreparedStatement prepSetStatus3;
	
	/**
	 * Eliminate Duplication Filter
	 */
	static HashSet<String> filter = null;

	public static HashSet<String> getFilter() {
		return filter;
	}
	private static org.apache.commons.logging.Log logSipderSQLWorkload = LogFactory.getLog(SpiderSQLWorkload.class);

	/**
	 * Create a new SQL workload store and connect to a database.
	 * 
	 * @param driver
	 *            The JDBC driver to use.
	 * @param source
	 *            The driver source name.
	 * @exception java.sql.SQLException
	 * @exception java.lang.ClassNotFoundException
	 */
	public SpiderSQLWorkload() throws SQLException,
			ClassNotFoundException {
		Log log = new Log();
		connection = MysqlDBConn.getMysqlDBConn().getConnection();
		prepClear = connection.prepareStatement("DELETE FROM tblworkload_test;");
		prepAssign = connection
				//	.prepareStatement("select url from tblworkload_test AS t1 JOIN(select ROUND(RAND()*0.6*((select MAX(id) FROM tblworkload_test)-" +
					//		"(select MIN(id) FROM  tblworkload_test))+(select MIN(id) FROM tblworkload_test))AS id)AS t2 where t1.id >= t2.id and status = 'W' ORDER BY t1.id LIMIT 1;");
		//随机的选取一行处于等待状态的队列
			.prepareStatement("SELECT url FROM tblworkload_test WHERE status = 'W' ORDER BY RAND() LIMIT 1;");
		resumeAssign = connection
				.prepareStatement("SELECT count(*) as cty FROM tblworkload_test WHERE status = 'W';");
		prepGetStatus = connection
				.prepareStatement("SELECT status FROM tblworkload_test WHERE url = ?;");
		prepSetStatus1 = connection
				.prepareStatement("SELECT count(*) as qty FROM tblworkload_test WHERE url = ?;");
		prepSetStatus2 = connection
				.prepareStatement("INSERT INTO tblworkload_test(uid,url,status) VALUES (?,?,?);");
		prepSetStatus3 = connection
				.prepareStatement("UPDATE tblworkload_test SET status = ? WHERE url = ?;");
		filter = GenerateFilter.getFilter();
	}

	/**
	 * Call this method to request a URL to process. This method will return a
	 * WAITING URL and mark it as RUNNING.
	 * 
	 * @return The URL that was assigned.
	 */
	 public String assignWorkload() {
		ResultSet rs = null;

		try {
			rs = prepAssign.executeQuery();

			if (!rs.next())
				return null;
			//String uid = "";
			String url = rs.getString("url");
		//	uid = MD5.getMD5(url);
			setStatus_1(url, RUNNING);
			//setStatus(url,RUNNING);
			return url;
		} catch (SQLException e) {
			Log.logger.error("SQL Error: ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Add a new URL to the workload, and assign it a status of WAITING.
	 * 
	 * @param url
	 *            The URL to be added.
	 */
	 /*
	 public void addWorkload(String url) {
		if(filter.contains(MD5.getMD5(url)))
			return;
		if (IsChina.Ipv6Filter(url)) {
			filter.add(MD5.getMD5(url));
		    setStatus(url, WAITING);
		}
	}
	 */
	 
	 
	  public void addWorkload(String url) {
		 long startTime = System.currentTimeMillis();
			  setStatus(url, WAITING);
		 long endTime = System.currentTimeMillis();
		 long lastTime = endTime - startTime;
		 logSipderSQLWorkload.info("db contains judge lastTime: " + lastTime);
	  }
	   
	  
	  

	/**
	 * Called to mark this URL as either COMPLETE or ERROR.
	 * 
	 * @param url
	 *            The URL to complete.
	 * @param error
	 *            true - assign this workload a status of ERROR. false - assign
	 *            this workload a status of COMPLETE.
	 */
	public void completeWorkload(String url, boolean error) {
		//String uid = MD5.getMD5(url);
		if (error)
			setStatus_1(url, ERROR);
		else
			setStatus_1(url, COMPLETE);
         
	}

	/**
	 * This is an internal method used to set the status of a given URL. This
	 * method will create a record for the URL of one does not currently exist.
	 * 
	 * @param url
	 *            The URL to set the status for.
	 * @param status
	 *            What status to set.
	 */
	protected void setStatus(String url, char status) {
		try {
				IsChina ic = new IsChina();
				String uid_tbl = ic.isChina(url);
				prepSetStatus2.setString(1,uid_tbl);
				prepSetStatus2.setString(2,url);
				prepSetStatus2.setString(3, (new Character(status)).toString());
				prepSetStatus2.executeUpdate();
					IsChina.Ipv6Filter(url);
		} catch (SQLException e) {
			Log.logger.error("SQL Error: ", e);
		}
	}

	protected void setStatus_1(String url, char status) {
	        // Update it
		try {
			prepSetStatus3.setString(1, (new Character(status)).toString());
			prepSetStatus3.setString(2, url);
			prepSetStatus3.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	/**
	 * Get the status of a URL.
	 * 
	 * @param url
	 *            Returns either RUNNING, ERROR WAITING, or COMPLETE. If the URL
	 *            does not exist in the database, the value of UNKNOWN is
	 *            returned.
	 * @return Returns either RUNNING,ERROR, WAITING,COMPLETE or UNKNOWN.
	 */
	public char getURLStatus(String url) {
		ResultSet rs = null;

		try {
			// first see if one exists
			prepGetStatus.setString(1, url);
			rs = prepGetStatus.executeQuery();

			if (!rs.next())
				return UNKNOWN;

			return rs.getString("status").charAt(0);
		} catch (SQLException e) {
			Log.logger.error("SQL Error: ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return UNKNOWN;
	}

	/**
	 * Clear the contents of the workload store.
	 */
	public void clear() {
		try {
			prepClear.executeUpdate();
		} catch (SQLException e) {
			Log.logger.error("SQL Error: ", e);
		}
	}
	
	/**
	 * Query the number of URLs which status is 'W' in the workload store.
	 */
	public int queryWorkload() {
		ResultSet rs = null;

		try {
			rs = resumeAssign.executeQuery();
			rs.next();
			int count = rs.getInt("cty");
            return count;
		} catch (SQLException e) {
			Log.logger.error("SQL Error: ", e);
			return 0;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
	
	}
}
