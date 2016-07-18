package com.hadoop.sinaSpider.download;

import com.hadoop.sinaSpider.bean.BlogByAuthor;
import com.hadoop.sinaSpider.bean.Page;

public interface Downloadable {
	BlogByAuthor downloadBlogByAuthor(String url);
	Page downloadPage(String url);
}
