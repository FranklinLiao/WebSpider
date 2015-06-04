package org.tools;

import org.tools.xml.*;

public class GenerateXML {

	/**
	 * 生成Spider所需的配置文件
	 */
	public GenerateXML() {
		XMLUtil xmlUtil = new XMLUtil();
		xmlUtil.InitXML();
		IPv6Extractor.getIpList("http://www-public.int-evry.fr/~maigron/RIR_Stats/RIR_Delegations/Delegations/IPv6/CN.html#APNIC");
	}
	
	
	public static void main(String args[]) {
		GenerateXML gx = new GenerateXML();
	}
	
}