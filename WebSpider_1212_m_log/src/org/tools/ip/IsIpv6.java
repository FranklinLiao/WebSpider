package org.tools.ip;

/**
 * 辅助判断给定URL是否为IPV6站点(可能包括双栈站点）
 * @author jwx 2011.4.25
 */
import java.net.*;

public class IsIpv6 {
	private boolean isIpv6 = false;
	private String urlString = "";
	private Inet6Address ipv6Address = null;

	public IsIpv6(String urlString) throws Exception {
		this.urlString = urlString;
		this.judge();
	}

	public void judge() throws Exception {
		if (urlString.length() == 0)
			return;
		String host = IsIpv6.GetDomainName(urlString);

		if (host.endsWith(".hk") || host.endsWith(".tw")
				|| host.endsWith(".jp") || host.endsWith(".kr")
				|| host.endsWith(".in") || host.endsWith(".uk")
				|| host.endsWith(".ru") || host.endsWith(".mx")
				|| host.endsWith(".it") || host.endsWith(".fr")
				|| host.endsWith(".us"))
			isIpv6 = false;
		else {
			InetAddress address = InetAddress.getByName(host);
			if (address instanceof Inet6Address) {
				isIpv6 = true;
				this.ipv6Address = (Inet6Address) address;
			}
		}
	}

	public static String GetDomainName(String url) {
		// 从URL中提取网站域名
		String s = url;
		try {
			URL strURL = new URL(url);
			s = strURL.getHost();
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException:" + e);
		}
		return s;
	}

	public boolean getIsIpv6() {
		return this.isIpv6;
	}

	public Inet6Address getIpv6() {
		return this.ipv6Address;
	}

}
