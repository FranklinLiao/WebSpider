package org.net.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode编码转中文字符串
 */
public class CodeTrans {

	public static String Trans(String code) {
		
		String str = code.replaceAll(";&#", ",").replace("&#", ",")
				.replaceAll(";", ",").replace(" ", "");
		String[] s2 = str.split(",");
		String s1 = "";
		// System.out.println(s2.length);

		for (int i = 1; i < s2.length; i++) {
			int v = Integer.parseInt(s2[i], 10);
			s1 = s1 + (char) v;
		}
		return s1;
	}

	/**
	 * 让带乱码的字符串正常显示
	 * 
	 * @param str
	 * @return
	 */
	public static String TransStr(String str) {
		String newStr = str.replaceAll("(&nbsp)+", "")
				.replaceAll("(&nbsp;)+", "").replace("......", ";").replaceAll("\"","");
		//System.out.println(newStr);
		Pattern p = Pattern.compile("(&#\\d{5};)+");
		// 用Pattern类的matcher()方法生成一个Matcher对象
		Matcher m = p.matcher(newStr);
		StringBuffer sb = new StringBuffer();
		// 使用find()方法查找第一个匹配的对象
		boolean result = m.find();
		// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
		while (result) {
			m.appendReplacement(sb, Trans(m.group()));
			result = m.find();
		}
		// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
		m.appendTail(sb);
		return sb.toString();
	}

	public static void main(String args[]) {
		String str = "&#21313;&#26376;&#22260;&#22478;2012&#21050;&#38517;"; 
		System.out.println(TransStr(str));
	}

}