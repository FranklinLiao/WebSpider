package org.tools.ip;

/**
 * IPv6地址解析类
 * @author JWX
 *
 */
public class Ipv6Convert {

	/**
	 * 获取标准IPv6地址二进制字符串前缀
	 * 
	 * @param ipStr
	 * @param len
	 * @return
	 */
	public static String getIpPrefix(String ipStr, int len) {
		String[] str1 = ipStr.split(":+");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < str1.length; i++) {
			if (str1[i].length() < 4) {
				StringBuffer addZero = new StringBuffer();
				int addZeroNum = 4 - str1[i].length();
				for (int j = 1; j <= addZeroNum; j++) {
					addZero.append("0");
				}
				sb.append(convertHexToBinary(addZero.toString() + str1[i]));
			} else
				sb.append(convertHexToBinary(str1[i]));
		}

		return sb.toString().substring(0, len);
	}
    
	/**
	 * 将IPv6地址字符串补全
	 * @param str
	 * @return
	 */
	public static String initIpStr(String[] str) {
		StringBuffer newStr = new StringBuffer();
		int addZeroNum = 8 - str.length;
		for (int i = 0; i < str.length; i++) {
			newStr.append(str[i]+":");
		}
		for (int j = 1; j <= addZeroNum; j++) {
			if(j != addZeroNum)
			   newStr.append("0"+":");
			else
			   newStr.append("0");
		}
		return newStr.toString();
	}

	/**
	 * 将十六进制字符串转换为标准二进制字符串
	 * 
	 * @param hexString
	 * @return
	 */
	public static String convertHexToBinary(String hexString) {
		long l = Long.parseLong(hexString, 16);
		String binaryString = Long.toBinaryString(l);
		int shouldBinaryLen = hexString.length() * 4;
		StringBuffer addZero = new StringBuffer();
		int addZeroNum = shouldBinaryLen - binaryString.length();
		for (int i = 1; i <= addZeroNum; i++) {
			addZero.append("0");
		}
		return addZero.toString() + binaryString;
	}

	/**
	 * 获得IP地址前缀的最终方法
	 * @param ipStr 输入的ip字符串可以是部分或完整的IPv6地址(::省略标记只能出现在尾端)
	 * @param len  要截取的二进制位长度
	 * @return
	 */
	public static String getPrefix(String ipStr, int len){
		String[] str1 = ipStr.split(":+");
		if (str1.length != 8) {
			ipStr = initIpStr(str1);
		}
		return getIpPrefix(ipStr,len);
	}
	
	
	/**
	 * 测试代码
	 * @param args
	public static void main(String args[]) {
		String str="2001:df0:27e";
		System.out.println(getPrefix(str,128));
	}
	*/
}
