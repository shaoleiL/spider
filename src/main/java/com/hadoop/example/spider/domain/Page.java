package com.hadoop.example.spider.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {
	
	/**
	 * 页面原始内容
	 */
	private String content;
	
	/**
	 * 页面url
	 */
	private String url;
	
	/**
	 * 保存商品的基本信息
	 */
	private Map<String, String> values = new HashMap<String, String>();
	
	
	/**
	 * 商品id
	 * 用作hbase的rowkey
	 */
	private String goodsId;
	
	
	/**
	 * 临时保存url
	 */
	private List<String> urls = new ArrayList<String>();
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void addField(String key,String value){
		this.values.put(key, value);
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public List<String> getUrls() {
		return urls;
	}
	public void addUrl(String url){
		this.urls.add(url);
	}
	
	
	
}
