package org.spider.SiteSpider;

import org.tools.GenerateXML;
import org.tools.Log;


/**
 * Spider类是蜘蛛程序的主控类,其负责: 各抓取线程的管理, 链接管理器的调度和分配;
 * 
 * @author f705 2011.5
 */
public class Spider extends Thread // 创建Spider类，继承Thread
{
	protected boolean halted = false;
	protected IWorkloadStorable workload;
	protected SpiderWorker pool[];
	/* 定义监听器,监听抓取各个线程开始和结束 */
	protected SpiderListener slistener = new SpiderListener();
	protected String charset;
	protected int deepth = 5;

	/**
	 * 构造函数,获取蜘蛛程序运行所需要的基本信息
	 * 
	 * @param manager
	 *            用户定义的链接管理器对象
	 * @param poolsize
	 *            线程池大小
	 * @param url
	 *            初始抓取的url
	 * @param charset
	 *            指定网页字符集编码
	 * @param deepth
	 *            指定网页抓取的深度
	 */
	public Spider(IWorkloadStorable w, int poolsize, String url,
			String charset, int deepth) {
		workload = w;
		pool = new SpiderWorker[poolsize];//10个spiderworker
		this.charset = charset;
		this.deepth = deepth;
		this.init(url);
	}

	/**
	 * 构造函数中调用的内部方法,负责初始化Spider程序
	 * 
	 * @param url
	 *            开始抓取的url,加入到链接管理器
	 */
	private void init(String url) {	
		System.out.println("开始生成xml文件...");
		GenerateXML gx = new GenerateXML();
		System.out.println("生成xml文件结束");
		for (int i = 0; i < pool.length; i++) {
			pool[i] = new SpiderWorker(i, this, charset, this.deepth);
		}
		int w = workload.queryWorkload();
		if(w==0) 
		{
		   if (url.length() > 0) {
			  workload.clear();// 将各个队列清空
			  addWorkload(url);// 将初始的一个或几个url加入waiting队列
		   }
		}
	}

	/**
	 * spider的主要循环部分,通过start方法被调用, 一直运行,直到链接队列中链接均被访问
	 */
	public void run() { // 线程真正完成功能的代码
		if (halted)
			return;
		for (int i = 0; i < pool.length; i++) {
			pool[i].start();// 启动SpiderWorker中的run（）
		}
		try {
			// 使用监听器监听全部抓取程序的开始和结束
			slistener.waitBegin();
			slistener.waitDone();
			Log.logger.info("Spider has no work.");
			System.out.println("Spider has no work.");

			for (int i = 0; i < pool.length; i++) {
				pool[i].interrupt();
				pool[i].join();
				pool[i] = null;
			}
		} catch (Exception e) {
			e = null;
		}
	}

	/**
	 * 该方法调用链接管理器从等待队列取得未被访问的链接,需考虑线程同步问题
	 * 
	 * @return 返回即将被访问的url
	 */
	synchronized public String getWorkload() {
		try {
			for (;;) {
				if (halted)
					return null;
				String w = workload.assignWorkload();
				if (w != null)
					return w;
				wait();
			}
		} catch (java.lang.InterruptedException e) {
		}
		return null;
	}

	/**
	 * 该方法主要功能是调用链接管理器, 将获得的链接加入到等待队列
	 * 
	 * @param url
	 *            即将加入到等待队列的链接
	 */
	synchronized public void addWorkload(String url) {
		if (halted)
			return;
		workload.addWorkload(url);
		notify();
	}

	/**
	 * 获得当前spider对象使用的监听器
	 * 
	 * @return 当前使用的监听器
	 */
	public SpiderListener getspiderListener() {
		return this.slistener;
	}

	/**
	 * 网页解析完毕,调用链接管理器,将链接移除运行队列,移入完成队列
	 * 
	 * @param url
	 *            即将移除运行队列,加入完成队列
	 * @param error
	 *            是否发生错误
	 */
	synchronized public void completePage(String url, boolean error) {
		workload.completeWorkload(url, error);
	}

	/**
	 * 停止当前Spider线程
	 */
	synchronized public void halt() {
		halted = true;
		workload.clear();
		notifyAll();
	}

}
