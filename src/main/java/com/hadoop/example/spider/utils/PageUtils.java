package com.hadoop.example.spider.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtils {
	static Logger logger = LoggerFactory.getLogger(PageUtils.class);

	/**
	 * 根据url获取页面内容
	 * @param url
	 * @return
	 */
	public static String getContent(String url){
		String content = null;
		//获取一个构建器，构建一个client对象
		HttpClientBuilder builder = HttpClients.custom();
		//代理ip和端口,不能写死，需要维护一个代理ip库，当发现代理ip失效的时候，从ip库中移除
		String ip = "51.255.197.171";
		int port = 8080;
		HttpHost proxy = new HttpHost(ip, port);
		CloseableHttpClient client = builder/*.setProxy(proxy )*/.build();
		//封装http请求
		HttpGet request = new HttpGet(url);
		try {
			long start_time = System.currentTimeMillis();
			//执行get请求
			CloseableHttpResponse response = client.execute(request);
			//获取页面内容实体对象
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			logger.info("页面下载成功，消耗时间：{},url：{}",System.currentTimeMillis()-start_time,url);
		}catch(HttpHostConnectException e){//代理ip失效的异常
			//吧代理ip从代理ip库中移除，或者只打印日志记录一下,这对抓取失败的url，记录日志，
			e.printStackTrace();
			logger.error("页面下载失败，url：{},具体的错误内容：{}",url,e.getMessage());
		}catch (Exception e) {
			logger.error("页面下载失败，url：{},具体的错误内容：{}",url,e.getMessage());
		}
		return content;
	}
}
