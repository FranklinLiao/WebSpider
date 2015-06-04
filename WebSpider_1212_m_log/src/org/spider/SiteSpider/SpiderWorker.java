package org.spider.SiteSpider;

/**
 * 实际抓取网页的类,由Spider类产生
 * @author f705 2011.5
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.net.util.CodeTrans;
import org.tools.Log;
import org.tools.db.ConneDB;

public class SpiderWorker extends Thread {
	protected Spider owner;
	private int threadNo;
	private String url;
	private String charset;
	private int deepth = 10;
	private static org.apache.commons.logging.Log logSpiderWorker = LogFactory.getLog(SpiderWorker.class);

	/**
	 * 构造函数
	 * 
	 * @param i
	 *            当前线程的标志,属于线程池中的第i个线程
	 * @param owner
	 *            拥有当前线程的Spider对象
	 * @param charset
	 *            字符集编码
	 * @param maxBodySize
	 *            网页大小限定
	 * @param deepth
	 *            网页深度
	 */
	public SpiderWorker(int i, Spider owner, String charset, int deepth) {
		Log log = new Log();
		this.owner = owner;
		this.threadNo = i;
		this.charset = charset;
		this.deepth = deepth;
	}

	/**
	 * SpiderWorker的主要循环运行程序,在Spider中被实例化为pool被启动
	 */
	public void run() {
		while (true) {// 一直循环
			url = this.owner.getWorkload();// Spider中的getLink（）
			//Log.logger.info("get link " + url);
			// System.out.println("get link "+url);
			if (url == null)
				return;
			// 启动对某url的工作
			this.owner.getspiderListener().workerBegin();
			long timeStart = System.currentTimeMillis();
			processPage();
			long timeEnd = System.currentTimeMillis();
			long timeLast = timeEnd - timeStart;
			logSpiderWorker.info("processpage lastTime: " + timeLast);
			// 结束对某url的工作
			this.owner.getspiderListener().workerEnd();
		}
	}

	/**
	 * 建立静态lnkFilter，取出audio，embed类型的连接（属于CompositeTag）
	 */
	static NodeFilter lnkFilter = new NodeFilter() {

		private static final long serialVersionUID = 1L;

		public boolean accept(Node node) {
			if (node instanceof AudioTag)
				return true;
			if (node instanceof EmbedTag)
				return true;

			return false;
		}
	};

	/**
	 * Create a new kind of tag:AudioTag about the audio
	 * 
	 * @author f705
	 */
	static class AudioTag extends CompositeTag {
		private static final long serialVersionUID = 1L;
		private static final String[] mIds = new String[] { "AUDIO" };
		private static final String[] mEndTagEnders = new String[] { "AUDIO",
				"DIV" };

		public String[] getIds() {
			return (mIds);
		}

		public String[] getEnders() {
			return (mIds);
		}

		public String[] getEndTagEnders() {
			return (mEndTagEnders);
		}

		public String getLink() {
			return super.getAttribute("href");
		}

		public String getMethod() {
			return super.getAttribute("method");
		}
	}

	/**
	 * Create a new kind of tag:Embed about the video
	 * 
	 * @author f705
	 * 
	 */
	static class EmbedTag extends CompositeTag {
		private static final long serialVersionUID = 1L;
		private static final String[] mIds = new String[] { "EMBED" };
		private static final String[] mEndTagEnders = new String[] { "EMBED",
				"DIV" };

		public String[] getIds() {
			return (mIds);
		}

		public String[] getEnders() {
			return (mIds);
		}

		public String[] getEndTagEnders() {
			return (mEndTagEnders);
		}

		public String getLink() {
			return super.getAttribute("href");
		}

		public String getMethod() {
			return super.getAttribute("method");
		}
	}

	/**
	 * to get the image link individually; (because ImageTag can't be seen as a
	 * CompositeTag)
	 * 
	 * @param imgparser
	 */
	private void GetImage(Parser parser) {
		NodeFilter imgfilter = new TagNameFilter("IMG");
		NodeList imgnodelist = null;
		try {
			imgnodelist = parser.extractAllNodesThatMatch(imgfilter);
		} catch (ParserException e) {
			// Print the error
			Log.logger.error(e);
		}
		//Log.logger.info("img list size" + imgnodelist.size());
		// System.out.println("img list size" + imgnodelist.size());
		for (int k = 0; k < imgnodelist.size(); k++) {

			ImageTag imglink = (ImageTag) imgnodelist.elementAt(k);
			//Log.logger.info("IMAGE:\t" + imglink.getImageURL());
			// System.out.println("IMAGE:\t" + imglink.getImageURL());

			String urlGet = imglink.getImageURL();
			if (IsValidate.isValidate(urlGet, this.deepth)) {
				// 规范化链接形式
				urlGet = new UrlFormatProcess(urlGet).Format();
				// 加入链接队列，同时也进行排重的操作
				this.owner.addWorkload(urlGet);
			}
		}// end for new end above is the parse of image.
	}

	/**
	 * to get the http link individually; (because LinkTag can't be seen as a
	 * CompositeTag)
	 * 
	 * @param imgparser
	 */
	private void GetLink(Parser parser) {
		NodeFilter linkfilter = new TagNameFilter("A");
		NodeList linknodelist = null;
		try {
			linknodelist = parser.extractAllNodesThatMatch(linkfilter);
		} catch (ParserException e) {
			// Print the error
			Log.logger.error(e);
		}
		// System.out.println("link list size" + linknodelist.size());
		//Log.logger.info("link list size" + linknodelist.size());
		for (int k = 0; k < linknodelist.size(); k++) {

			LinkTag link = (LinkTag) linknodelist.elementAt(k);
			//Log.logger.info("Link:\t" + link.getLink());
			// System.out.println("Link:\t" + link.getLink());

			String urlGet = link.getLink();
			if (IsValidate.isValidate(urlGet, this.deepth)) {
				// 规范化链接形式
				urlGet = new UrlFormatProcess(urlGet).Format();
				// 加入链接队列，同时也进行排重的操作
				this.owner.addWorkload(urlGet);
			}
		}// end for new end above is the parse of image.
	}

	/**
	 * 解析网页的主要方法,利用HTMLParser,具体的使用可参照HTMLParser的官方网站 解析当前网页上的所有符合要求的链接
	 */
	public void processPage() {
		if (!(this.url == null)) {
			boolean isValidate = false;
			boolean error = false;
			boolean isWell = false;
			boolean isWeb = false;
			/**
			 * 判断当前url是否有效, 本程序为了防止解析多媒体文件,将剔除doc pdf mpg 等文件格式, 限定了深度
			 */
			isValidate = IsValidate.isValidate(this.url, this.deepth);
			/*
			 * //判断是否是网页，如果不是网页不进行页面解析 IsWeb isWeb; try { isWeb = new
			 * IsWeb(this.url); isw= isWeb.getIsWeb(); } catch (IOException e2)
			 * { e2.printStackTrace(); }
			 */
			if (isValidate) {
				GetHTML getHtml = new GetHTML(); // 获取HTTP响应消息头部信息
				getHtml.getContent(this.url);
				isWell = getHtml.isWell();
				isWeb = getHtml.isWeb();
			  if(isWeb){
				try {
					URL url = new URL(this.url);
					HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
		            urlcon.setConnectTimeout(10000);
		            urlcon.setReadTimeout(20000);     //设置读取网页数据超时，防止HtmlParser假死
		            //urlcon.connect();*/
		            Parser parser = new Parser();
		           
					PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
					factory.registerTag(new AudioTag());
					parser.setNodeFactory(factory);

					PrototypicalNodeFactory factory2 = new PrototypicalNodeFactory();
					factory.registerTag(new EmbedTag());
					parser.setNodeFactory(factory2);

					try {
						 parser.setConnection(urlcon);
						 //parser.setResource(this.url);
				         parser.setEncoding(parser.getEncoding());   
						// WebPageReader wpr = new
						// WebPageReader(this.url,charset,this.maxBodySize);
						// wpr.getHtmlCode();

						//String charSet = getHtml.getCharset(this.url);
						//parser.setEncoding(charSet);
						// if(!wpr.getIsTooLong()&&
						// getHtml.getContent(this.url)){//如果网页没有超过限定长度且能正常读取HTTP响应消息
						if (isWell) {
							//Log.logger.info("parse url : " + url);
							// System.out.println("parse url : "+url);
							HtmlPage page = new HtmlPage(parser);// 开始parse网页
							parser.visitAllNodesWith(page);
							// 取得网页标题信息
							String title = CodeTrans.TransStr(page.getTitle());
							// 存储Title信息到数据库
							ConneDB td = new ConneDB();
							td.updateTitleByUrl(title, this.url);
							parser.reset();

							// 遍历符合条件的所有节点
							NodeList nlist = parser
									.extractAllNodesThatMatch(lnkFilter);

							for (int i = 0; i < nlist.size(); i++) {
								CompositeTag node = (CompositeTag) nlist
										.elementAt(i);

								if (node instanceof AudioTag) {
									AudioTag audio = (AudioTag) node;
									//Log.logger.info("AUDIO: \t"+ audio.getLink());
									// System.out.println("AUDIO: \t"
									// + audio.getLink());
									String urlGet = audio.getLink();
									if (IsValidate.isValidate(urlGet,
											this.deepth)) {
										// 规范化链接形式
										urlGet = new UrlFormatProcess(urlGet)
												.Format();
										// 加入链接队列，同时也进行排重的操作
										this.owner.addWorkload(urlGet);
									}// end if
								} else if (node instanceof EmbedTag) {
									EmbedTag embed = (EmbedTag) node;
									//Log.logger.info("EMBED:\t"+ embed.getLink());
									// System.out.println("EMBED:\t"
									// + embed.getLink());
									String urlGet = embed.getLink();
									if (IsValidate.isValidate(urlGet,
											this.deepth)) {
										// 规范化链接形式
										urlGet = new UrlFormatProcess(urlGet)
												.Format();
										// 加入链接队列，同时也进行排重的操作
										this.owner.addWorkload(urlGet);
									}// end if
								}
							}
							// get the link and image
							parser.reset();
							this.GetLink(parser);
							parser.reset();
							this.GetImage(parser);
							parser.reset();

							Log.logger.info("parse is over");
							// System.out.println("parse is over");
						}// end if
						else
							error = true;
					} catch (ParserException e1) {
						error = true;
						Log.logger.error(e1);
					}// end try
				} catch (IOException ioe) {
					error = true;
					Log.logger.error(ioe);
				} catch (Exception e) {
					error = true;
					Log.logger.error(e);
				}
			  }
			}
			// 完成当前网页解析
			this.owner.completePage(this.url, error);
		}
	}
}
