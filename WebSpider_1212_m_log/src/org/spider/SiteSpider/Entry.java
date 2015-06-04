package org.spider.SiteSpider;

import java.util.Properties;

import org.net.util.PropertyUtil;
import org.net.util.ShutDownWork;

/**
 * 程序入口 main 函数,定义一个蜘蛛(spider)和一个链接管理器,引导程序开始运行.
 * 
 * @author simlab 2008.4.5
 */
public class Entry {
	/**
	 * @variable poolsize:线程池大小,程序利用多线程抓取网络上网页资源
	 * @variable url:初始入口链接地址,通常选取网站的主页和sitemap作为入口
	 * @variable charset:初始定义的编码字符集,默认为中文编码GB2312
	 * @variable lmanager:定义链接管理器,管理链接队列:等待队列, 运行队列和完成队列
	 * @variable s:定义蜘蛛,管理调度整个蜘蛛程序的运行
	 * @variable deepth:定义链接的深度,本文采取简单的方式,利用链接地址中的/作为链接深度的标志,默认是5.如
	 *           http://a/b/c/d/e/f/index.htm 将不加以解析,
	 *           因为除了http://中含有的/,其余含有6个/,超出了设置的的深度. 这样防止抓取时候陷入过多的深度链接.
	 *           较为正式的做法是根据获取该链接的经过的链接数判断深度的.
	 */
	public static void main(String args[]) throws Exception{
		//设置读取初始化配置文件
		Properties prop = PropertyUtil.readProperty("/config/init.properties");
		int poolsize = Integer.parseInt(prop.getProperty("poolsize"));
		String url = prop.getProperty("start_url");
		String charset = "utf-8";
		int deepth = Integer.parseInt(prop.getProperty("link_deepth"));
		java.lang.System.setProperty("java.net.preferIPv6Addresses", "true");//设置允许ipv6
		Runtime.getRuntime().addShutdownHook(new ShutDownWork());
		IWorkloadStorable lw = new SpiderSQLWorkload();
		Spider s = new Spider(lw, poolsize, url, charset, deepth);
		s.start();
	}
}
