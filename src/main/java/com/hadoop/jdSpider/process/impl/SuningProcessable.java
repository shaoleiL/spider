package com.hadoop.jdSpider.process.impl;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.hadoop.jdSpider.Page;
import com.hadoop.jdSpider.process.Processable;

public class SuningProcessable implements Processable {

	@Override
	public void process(Page page) {
		String context = page.getContext();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(context);
		String url = page.getUrl();

	}

}
