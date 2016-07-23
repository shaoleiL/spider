package com.hadoop.example.spider.store;

import com.hadoop.example.spider.domain.Page;

public class ConsoleStore implements Storeable{

	@Override
	public void store(Page page) {
		System.out.println(page.getUrl()+"----"+page.getValues().get("price"));
	}

}
