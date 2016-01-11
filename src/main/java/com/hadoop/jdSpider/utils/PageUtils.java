package com.hadoop.jdSpider.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtils {

	private static Logger logger = LoggerFactory.getLogger(PageUtils.class);
	
	public static String getContext(String url){
		String context = "";
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		HttpGet httpGet = new HttpGet(url);
		try {
			long starttime = System.currentTimeMillis();
			CloseableHttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			context = EntityUtils.toString(entity);
			logger.info(Thread.currentThread().getId() +"页面下载成功:{},消耗时间:{}",url,System.currentTimeMillis() - starttime);
		} catch (ParseException | IOException e) {
			logger.error("页面下载失败:{}",url);
			e.printStackTrace();
		}
		return context;
	}
}
