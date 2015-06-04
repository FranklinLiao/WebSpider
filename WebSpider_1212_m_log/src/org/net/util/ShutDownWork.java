package org.net.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.spider.SiteSpider.SpiderSQLWorkload;
/**
 * 程序结束时需要执行的工作 <br>
 * @author JWX
 */
public class ShutDownWork extends Thread {
	/**
	 * This is the right work that will do before the system shutdown
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("filter.obj"));
			out.writeObject(SpiderSQLWorkload.getFilter());
			out.close();
		} catch (IOException ex) {
			System.err.println("exception at ShutDownWork#run()...");
		}
	}
	
}
