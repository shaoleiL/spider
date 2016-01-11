package com.hadoop.jdSpider.download;

import com.hadoop.jdSpider.Page;
import com.hadoop.jdSpider.utils.PageUtils;

public class HttpClientDownload implements Downloadable{

	@Override
	public Page download(String url) {
		String context = PageUtils.getContext(url);
		Page page = new Page();
		page.setContext(context);
		page.setUrl(url);
		return page;
	}

}
