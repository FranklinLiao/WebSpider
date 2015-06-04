package org.spider.SiteSpider;
/**
 * 链接格式处理器:
 *  1.处理链接最后是/的形式,规范为不加/形式
 *    -- http://www.whu.edu.cn/ => http://www.whu.edu.cn
 *    -- http://www.whu.edu.cn/whu/ => http://www.whu.edu.cn/whu
 *  2.处理含有页面锚点的链接地址,去除链接锚点
 *    -- http://www.whu.edu.cn/index.php#123 =>
 *    http://www.whu.edu.cn/index.php
 *  3.规范URL中主机部分为IP地址的情况
 * @author f705 2011.5
 *
 */

public class UrlFormatProcess {
	private String url="";
	
	public UrlFormatProcess(String url){
	   this.url=url;
	}
	
	public String Format(){
	   if((this.url!=null)&&(this.url.lastIndexOf("/")== this.url.length()-1))//规范为不加/形式
	       this.url=this.url.substring(0,this.url.length()-1);
	   if(this.url.contains("#"))//去除链接锚点：指向同一页面的连接
	       this.url=this.url.substring(0,this.url.lastIndexOf("#"));
	   //规范URL中主机部分为IP地址的情况
	   int start=this.url.indexOf("//");
	   int end=this.url.indexOf("/",start+2);
	      if(end==-1)  end=this.url.length();
	   int chr=this.url.charAt(start+2);
	   String sub=this.url.substring(start+2, end);
	   String sub4=sub.substring(0,4);
	   if(sub4.indexOf(".")==-1)
	   {   StringBuffer urlbuffer = new StringBuffer(this.url);
	       if(chr>48&&chr<57&&(chr+1>48)&&(chr+1<57)&&(chr+2>48)&&(chr+2<57)&&(chr+3>48)&&(chr+3<57))
	       { 
	    	  urlbuffer=urlbuffer.replace(start+2,end, "["+sub+"]");
	          this.url = urlbuffer.toString();
	       }
	   }
	   //为了防止蜘蛛陷入过深的论坛链接中，对一些BBS站点进行限制
	   if(this.url.contains(".voc.com.cn"))
	   {
		   this.url=this.url.substring(0,this.url.indexOf("?"));
	   }
	   return this.url;
	}
	
}