package com.hadoop.example.spider.download;

import com.hadoop.example.spider.domain.Page;
import com.hadoop.example.spider.utils.PageUtils;

public class HttpclientDownloadImpl implements Downloadable {

	@Override
	public Page download(String url) {
		Page page = new Page();
		String content = PageUtils.getContent(url);
		page.setContent(content);
		page.setUrl(url);
		return page;
	}

}
