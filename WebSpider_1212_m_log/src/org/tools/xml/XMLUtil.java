package org.tools.xml;

import java.io.File;
import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.net.util.Dates;
import org.tools.Log;

/**
 * XML文件操作相关类
 * @author JWX
 *
 */
public class XMLUtil {

	/**
	 * 初始化XML文件
	 */
	public void InitXML() {
		// 创建ipv6blocks元素，并设置为XML文档的根节点
		Element ipv6blocks = new Element("ipv6blocks");
		
		Document myDocument = new Document(ipv6blocks);

		try {
			// 创建XML文件输出流
			XMLOutputter fmt = new XMLOutputter();
			// 创建文件输出流
			FileWriter writer = new FileWriter("ipv6blocks("+Dates.getDate()+").xml");
			// 设置所创建的XML文档的格式
			Format f = Format.getPrettyFormat();
			fmt.setFormat(f);
			// 将生成的XML文档写入到"ipv6blocks.xml"文件中
			fmt.output(myDocument, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改XML文件
	 */
	public void ModifyXML(String id, String block, String num, String descr) {
		try {
			SAXBuilder builder = new SAXBuilder();
			String filePath = "ipv6blocks("+Dates.getDate()+").xml";
			
			File file = new File(filePath);

			if (file.exists()) {
				Document document = (Document) builder.build(file);
				Element root = document.getRootElement();
				Element ipblock = new Element("ipv6block").setAttribute("id", id);
				ipblock.addContent(new Element("block").addContent(block));
				ipblock.addContent(new Element("num").addContent(num));
				ipblock.addContent(new Element("descr").addContent(descr));
				root.addContent(ipblock);

				XMLOutputter fmt = new XMLOutputter();
				Format f = Format.getPrettyFormat();
				fmt.setFormat(f);
				String xmlFileData = fmt.outputString(document);
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(xmlFileData);
				fileWriter.close();
			} else {
				System.out.println("File does not exist");
				Log.logger.error("File does not exist ！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public static void main(String args[]) {
		try {
			new XMLUtil().InitXML();
			new XMLUtil().ModifyXML("1", "2001:250::/35", "/35",
					"CERNET IPv6 Backbone");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
          
}
