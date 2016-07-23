package com.hadoop.example.spider;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hadoop.example.spider.store.ConsoleStore;
import com.hadoop.example.spider.store.HbaseStore;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hadoop.example.spider.domain.Page;
import com.hadoop.example.spider.download.Downloadable;
import com.hadoop.example.spider.download.HttpclientDownloadImpl;
import com.hadoop.example.spider.process.JdProcessImpl;
import com.hadoop.example.spider.process.Processable;
import com.hadoop.example.spider.repository.QueueRepository;
import com.hadoop.example.spider.repository.Repository;
import com.hadoop.example.spider.store.Storeable;
import com.hadoop.example.spider.utils.Config;
import com.hadoop.example.spider.utils.SleepUtils;

public class Spider {
	Logger logger = LoggerFactory.getLogger(Spider.class);
	
	
	public Spider() {
		String connectString = "192.168.1.171:2181,192.168.1.172:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		int sessionTimeoutMs = 5000;//会话超时时间，默认40S。这个值必须在4S--40S之间
		int connectionTimeoutMs = 1000;
		//获取当前服务器的ip信息
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			String ip = localHost.getHostAddress();
			//获取客户端连接
			CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
			client.start();
			client.create()
			.creatingParentsIfNeeded()//如果父节点不存在，则创建
			.withMode(CreateMode.EPHEMERAL)//指定节点类型，临时节点
			.withACL(Ids.OPEN_ACL_UNSAFE)//指定节点权限
			.forPath("/spider/"+ip);//指定节点名称
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 提取接口的步骤
	 * 1：定义接口
	 * 2：写实现类
	 * 3：在spider中对接口提供set方法
	 * 4：在使用爬虫的时候给接口注入实现类
	 */
	private Downloadable downloadable = new HttpclientDownloadImpl();
	private Processable processable;
	private Storeable storeable = new ConsoleStore();
	private Repository repository = new QueueRepository();
	
	/**
	 * 初始化一个固定大小的线程池
	 * 具体线程的个数一般是CPU核数的整数倍，建议1倍或者2倍
	 */
	ExecutorService threadPool = Executors.newFixedThreadPool(Config.nThread);

	public void start() {
		check();
		logger.info("爬虫开始运行....");
		while(true){
			final String url = repository.poll();
			if(url==null){
				logger.info("没有url了，休息一会。");
				SleepUtils.sleep(Config.millions_5);
			}else{
				threadPool.execute(
						new Runnable() {
							public void run() {
								Page page = Spider.this.download(url);
								Spider.this.process(page);
								List<String> urls = page.getUrls();
								for (String nextUrl : urls) {
									Spider.this.repository.add(nextUrl);
								}
								if (urls.isEmpty()) {//表示是商品明细页面
									Spider.this.store(page);
								}
								SleepUtils.sleep(Config.millions_1);
							}
						}
					);
				
			}
		}
	}
	
	/**
	 * 校验基础环境
	 * 使用log4j实现配置检查
	 */
	private void check() {
		logger.info("开始进行配置检查...");
		if(processable==null){
			String message = "没有设置默认解析类....";
			logger.error(message);
			throw new RuntimeException(message);
		}
		logger.info("=================================================");
		logger.info("downloadable的实现类是：{}",downloadable.getClass().getName());
		logger.info("processable的实现类是：{}",processable.getClass().getName());
		logger.info("storeable的实现类是：{}",storeable.getClass().getName());
		logger.info("repository的实现类是：{}",repository.getClass().getName());
		logger.info("=================================================");
	}

	/**
	 * 下载
	 * @param url 
	 * @return 
	 */
	public Page download(String url) {
		return this.downloadable.download(url);
	}
	/**
	 * 解析
	 * @param page 
	 */
	public void process(Page page) {
		this.processable.process(page);
	}
	/**
	 * 存储
	 * @param page 
	 */
	public void store(Page page) {
		this.storeable.store(page);
	}

	public void setDownload(Downloadable download) {
		this.downloadable = download;
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}

	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}
	
	
	public void setSeedUrl(String url){
		this.repository.add(url);
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		Spider spider = new Spider();
		spider.setProcessable(new JdProcessImpl());
		spider.setStoreable(new HbaseStore());
		String url = "http://list.jd.com/list.html?cat=9987,653,655&page=54&JL=6_0_0";
		spider.setSeedUrl(url);
		spider.start();
	}
}
