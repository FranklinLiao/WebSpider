package org.tools.ip;

/**
 * IPv6地址块的实体类
 * @author JWX
 *
 */
public class Ipv6Block {
	private String fixStr;
	private String prefix;
	private int length;
	 
	public Ipv6Block() {
	}
   
	public Ipv6Block(String ipStr) {
		String[] splitStr = ipStr.split("/");
		this.length = Integer.parseInt(splitStr[1].trim());
		this.fixStr = Ipv6Convert.getPrefix(splitStr[0],length);
		this.prefix = ipStr.substring(0, ipStr.indexOf(":"));
	}
	
	public String getFixStr() {
		return fixStr;
	}

	public void setFixStr(String fixStr) {
		this.fixStr = fixStr;
	}
	
	public String getPreFix() {
		return prefix;
	}

	public void setPreFix(String prefix) {
		this.prefix = prefix;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
