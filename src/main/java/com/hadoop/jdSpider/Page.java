package com.hadoop.jdSpider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {

	/**
	 *商品URL
	 */
	private String url;
	
	/**
	 *网页内容
	 */
	private String context;
	
	/**
	 * 商品的基本信息
	 */
	private Map<String,String> fieldMap = new HashMap<String, String>();
	
	/**
	 * 商品id
	 */
	private String goodsid;
	
	/**
	 * 下一页和当前页面的商品URL
	 */
	private List<String> urlList = new ArrayList<String>();
	
	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	public void addField(String name, String value){
		this.fieldMap.put(name, value);
	}
	
	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	
	public void addNextUrl(String url){
		this.urlList.add(url);
	}

	public List<String> getUrlList() {
		return urlList;
	}
	
}
