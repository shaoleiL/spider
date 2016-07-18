package com.hadoop.sinaSpider.process;

import com.hadoop.sinaSpider.bean.BlogByAuthor;
import com.hadoop.sinaSpider.bean.Page;

public interface Processable {

	void processPage(Page page);

    void processBlogByAuthor(BlogByAuthor blogByAuthor);
}
