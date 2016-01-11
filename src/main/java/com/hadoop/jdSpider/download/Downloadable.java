package com.hadoop.jdSpider.download;

import com.hadoop.jdSpider.Page;

public interface Downloadable {
	public Page download(String url);
}
