package org.tools;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.LogFactory;
import org.tools.db.ConneDB;
import org.tools.ip.Ipv6Convert;
import org.tools.ip.IsIpv6;
//----------
import org.tools.MD5;

import com.mysql.jdbc.CommunicationsException;

/**
 * 辅助判断网页是否属于中国大陆地区的IPV6站点(根据中国IPv6地址统计表数据)
 * 
 * @author JWX 2011.10  
 */

public class IsChina {
	private boolean isChina = false;
   // String uid = "";
	private static org.apache.commons.logging.Log logIsChina = LogFactory.getLog(IsChina.class);
	public IsChina() {
		Log log = new Log();
	}
	public String  isChina(String urlString) {
		boolean isv6 = false;
		String ipString = "";
		String agency = "";
		//--------------
		String uid="";
		//-----------------
		ConneDB td = new ConneDB();

		try {
			URL strURL = new URL(urlString);
			IsIpv6 isIpv6 = new IsIpv6(urlString);
			isv6 = isIpv6.getIsIpv6();
			Inet6Address ipv6Addr = isIpv6.getIpv6();
			if (ipv6Addr != null)
				ipString = ipv6Addr.getHostAddress();
			if (isv6) {
				if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 32))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 32));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 32)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 48))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 48));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 48)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 35))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 35));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 35)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 28))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 28));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 28)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 34))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 34));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 34)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 33))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 33));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 33)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 31))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 31));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 31)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 29))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 29));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 29)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 24))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 24));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 24)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 22))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 22));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 22)));
					isChina = true;
				} else if (GenerateMap.getFilter().contains(
						Ipv6Convert.getPrefix(ipString, 20))) {
					agency = GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 20));
					Log.logger.info(GenerateMap.getHm().get(
							Ipv6Convert.getPrefix(ipString, 20)));
					isChina = true;
				} else
					isChina = false;
			}
			if (isChina) {
				// 存储IPV6地址及可能存在的IPV4地址
				InetAddress addresses[] = InetAddress.getAllByName(strURL
						.getHost());
				String ipv4Str = null;
				for (InetAddress ipAddress : addresses)
					if (ipAddress instanceof Inet4Address) {
						ipv4Str = ipAddress.getHostAddress();
						break;
					}
				/*td.insertIpv6ByName(urlString, strURL.getHost(), ipString,
						ipv4Str, agency, new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(new Date()));
				*/
				//----------------
				 /*String s = urlString + ipString + ipv4Str;
				 uid = MD5.getMD5(s);*/
				//	uid = MD5.getMD5(urlString); //uid在sql中类型为varchar(16)或varchar(2)
					td.insertIpv6ByName(urlString,strURL.getHost(),ipString,
							ipv4Str, agency, new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss").format(new Date()));
				//----------------
			
			}
		} catch (UnknownHostException e) {
			Log.logger.error("UnknownHostException:" + e);
		} catch (CommunicationsException e) {
			Log.logger.error("CommunicationsException" + e);
		} catch (Exception e) {
			Log.logger.error(e);
		}
		return uid;
	}

	public static boolean Ipv6Filter(String url) {
		long timeStart = System.currentTimeMillis();
		IsChina ic = new IsChina();
		ic.isChina(url);
		long timeEnd = System.currentTimeMillis();
		long timeLast = timeEnd - timeStart;
		logIsChina.info("IsChina judge lastTime: " + timeLast);
		return ic.isChina;
	}

/*	public String getUid(String url) {
		String uid_1 = isChina(url);
		return uid_1;
	}*/
	
/**
 * 测试代码
 * @param args
 public static void main(String[] args) {
	 IsChina ic = new IsChina();
	 ic.isChina("bt.neu6.edu.cn");
	 System.out.println(ic.isChina);
	// System.out.println(isChina("2001:7fa:5:100:0:0:0:80")); 
  }
  */
}
