package com.hadoop.sinaSpider.bean;

import java.util.ArrayList;
import java.util.List;

public class Page {

	/**
	 * 博客id
	 */
	private String id;

	/**
	 *URL
	 */
	private String url;
	
	/**
	 *网页内容
	 */
	private String context;

	/**
	 * 发布时间
	 */
	private String releaseDate ;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 正文
	 */
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
