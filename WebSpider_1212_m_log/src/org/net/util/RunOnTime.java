package org.net.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.spider.SiteSpider.SpiderSQLWorkload;

/**
 * java 定时执行任务，java定时器
 * @author JWX
 *
 */
public class RunOnTime {

	/**
	 * 
	 * java 定时执行
	 * 
	 */
	public static void operateOnTime() {
		// 定时执行的任务
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// 在此代码内调用要执行任务的代码 ...
				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("filter.obj"));
					out.writeObject(SpiderSQLWorkload.getFilter());
					out.close();
				} catch (IOException ex) {
					System.err.println("exception at ShutDownWork#run()...");
				}
			}
		};

		// 创建一个定时器
		Timer timer = new Timer();

		// 设置在多长时间以后执行，然后每个多长时间重复执行
		// 设值 5 秒钟后开始执行第一次，以后每隔 2 秒钟执行一次
		// timer.schedule(task, 5 * 1000, 2 * 1000);
		// 设置从某一时刻开始执行，然后每隔多长时间重复执行
		// 设置从当前时间开始执行，然后每隔2 秒钟执行一次
		   timer.schedule(task, Calendar.getInstance().getTime(), 2 * 1000);
	}

}