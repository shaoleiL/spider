package com.hadoop.jdSpider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hadoop.jdSpider.Thread.ThreadPool;
import com.hadoop.jdSpider.Thread.impl.FixedThreadPoolImpl;
import com.hadoop.jdSpider.download.Downloadable;
import com.hadoop.jdSpider.download.HttpClientDownload;
import com.hadoop.jdSpider.process.Processable;
import com.hadoop.jdSpider.process.impl.JDProcessable;
import com.hadoop.jdSpider.repository.Repositoryable;
import com.hadoop.jdSpider.repository.impl.QueueRepositoryImpl;
import com.hadoop.jdSpider.store.Storeable;
import com.hadoop.jdSpider.store.impl.StoreImpl;
import com.hadoop.jdSpider.utils.Config;
import com.hadoop.jdSpider.utils.SleepUtils;

public class Spider {

	private Downloadable downloadable = new HttpClientDownload();	//下载
	private Processable processable;	//解析
	private Storeable storeable ;	//存储
	private Repositoryable repository = new QueueRepositoryImpl(); //存储的数据库
	private ThreadPool threadPool = new FixedThreadPoolImpl();	//多线程
	Logger logger = LoggerFactory.getLogger(getClass());	//日志
	
	public static void main(String[] args) {
		String url = "http://list.jd.com/list.html?cat=9987,653,655";
		Spider spider = new Spider();
		spider.setProcessable(new JDProcessable());
		spider.setStoreable(new StoreImpl());
		spider.setSeedUrl(url);
		spider.start();
	}
	
	
	public void start() {
		//check();
		logger.info("爬虫启动...");
		while(!Thread.currentThread().isInterrupted()){
			final String url = repository.poll();
			if(url!=null){
				SleepUtils.sleep(Config.million_1);
				threadPool.execute(
					new Runnable() {
						public void run() {
							Page page = Spider.this.downloadable.download(url);
							Spider.this.processable.process(page);
							List<String> urlList = page.getUrlList();
							for (String nextUrl : urlList) {
								repository.add(nextUrl);
							}
							Spider.this.storeable.store(page);
						}
					}
				);
			}else {
				SleepUtils.sleep(Config.million_1);
				System.out.println("没有了");
			}
		}
	}

	/**
	 * @方法名: check 
	 * @描述: 配置检查
	 * @返回值类型: void
	 * @创建时间: 2015年9月5日 
	 * @创建人: 李绍磊
	 */
	private void check() {
		if(processable==null){
			String message = "processable: 需要配置解析类.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		if(storeable==null){
			String message = "storeable: 需要配置解析类.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		logger.info("=========================================================");
		logger.info("downloadable使用的实现类:"+downloadable.getClass().getSimpleName());
		logger.info("processable使用的实现类:"+processable.getClass().getSimpleName());
		logger.info("storeable使用的实现类:"+storeable.getClass().getSimpleName());
		logger.info("threadPool使用的实现类:" + threadPool.getClass().getSimpleName());
		logger.info("repository使用的实现类:"+repository.getClass().getSimpleName());
		logger.info("=========================================================");
		
	}

	/**
	 * @方法名: download 
	 * @描述: 下载页面的快照
	 * @参数: @param url 
	 * @返回值类型: void
	 * @创建时间: 2015年8月27日 
	 * @创建人: 李绍磊
	 */
	public Page download(String url) {
		
		return this.downloadable.download(url);
		
	}

	/**
	 * @方法名: process 
	 * @描述: 解析
	 * @参数: @param page 
	 * @返回值类型: void
	 * @创建时间: 2015年8月27日 
	 * @创建人: 李绍磊
	 */
	public void process(Page page) {
		this.processable.process(page);
		
	}

	/**
	 * @方法名: store 
	 * @描述: 存储
	 * @参数: @param page 
	 * @返回值类型: void
	 * @创建时间: 2015年8月27日 
	 * @创建人: 李绍磊
	 */
	public void store(Page page) {
		this.storeable.store(page);
		
	}
	
	/**
	 * @方法名: setSeedUrl 
	 * @描述: 入口的url
	 * @参数: @param url 
	 * @返回值类型: void
	 * @创建时间: 2015年9月4日 
	 * @创建人: 李绍磊
	 */
	public void setSeedUrl(String url){
		this.repository.add(url);
	}

	public Downloadable getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}

	public Processable getProcessable() {
		return processable;
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}

	public Storeable getStoreable() {
		return storeable;
	}

	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}

	public Repositoryable getRepository() {
		return repository;
	}

	public void setRepository(Repositoryable repository) {
		this.repository = repository;
	}

}
