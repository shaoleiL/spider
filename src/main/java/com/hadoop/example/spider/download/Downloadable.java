package com.hadoop.example.spider.download;

import com.hadoop.example.spider.domain.Page;

public interface Downloadable {
	
	public Page download(String url);
}
