package org.spider.SiteSpider;
/**
 * 线程监听器:
 * 负责监听各个线程处理是否完成
 * 各个子线程的开始和结束
 * 线程的当前状态
 * @author f705 2011.5
 */
public class SpiderListener {
	//当前活动的(运行的)线程
	private int activeThreads = 0;
	//spider线程是否运行
	private boolean started = false;
	
	//判断各个线程是否都结束
	synchronized public void waitDone(){
	    try {
	       while ( activeThreads>0 ) {
	         wait();
	       }
	    } catch ( InterruptedException e ) {
	  }
	}

   /**
    * 判断Spider线程是否开始运行,此方法返回,则Spider程序正式运行
    */
    synchronized public void waitBegin(){
      try {
         while ( !started ) {
           wait();
         }
      } catch ( InterruptedException e ) {
      }
    }

   /**
    * 指示每个SpiderWorker线程的开始，同步非常重要！！
    */
   synchronized public void workerBegin(){
       activeThreads++;
       started = true;
       notify();
   }

   /**
    * 指示每个SpiderWorker线程的结束
    */
   synchronized public void workerEnd(){
       activeThreads--;
       notify();
   }

   /**
    * 线程状态重置
    */
   synchronized public void reset(){
       activeThreads = 0;
   }
}